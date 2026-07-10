package net.typho.big_shot_lib.intellij.codecs

import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.ResolveResult
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.Unmodifiable

object CodecLookupReferenceProvider : PsiReferenceProvider() {
    const val IDENTIFIER_REGEX = "[a-z0-9_.-]"

    @JvmField
    val CACHE_KEY = Key.create<CachedValue<List<PsiMethodCallExpression>>>("big_shot_lib:codec_fields")
    @JvmField
    val CODEC_CLASSES = mutableSetOf(
        "com.mojang.serialization.Encoder",
        "com.mojang.serialization.Decoder",
        "com.mojang.serialization.MapCodec"
    )

    @JvmField
    val KNOWN_CODECS = mutableMapOf<Regex, (element: JsonStringLiteral, json: JsonObject?, candidate: PsiMethodCallExpression) -> Boolean>()

    init {
        defineResourceType("data", "tags", ".json", hashSetOf("net.minecraft.tags.TagFile"))
        defineResourceType("data", "recipe", ".json") { element, json ->
            val typeProperty = (json ?: return@defineResourceType emptySet()).findProperty("type") ?: return@defineResourceType emptySet()
            val type = ((typeProperty.value ?: return@defineResourceType emptySet()) as? JsonStringLiteral) ?: return@defineResourceType emptySet()
            val classes = mutableSetOf(
                "net.minecraft.world.item.crafting.Recipe"
            )

            when (type.value) {
                "minecraft:crafting_shaped" -> {
                    classes.add("net.minecraft.world.item.crafting.CraftingRecipe")
                    classes.add("net.minecraft.world.item.crafting.ShapedRecipePattern")
                    classes.add("net.minecraft.world.item.crafting.ShapedRecipe")
                }
                "minecraft:crafting_shapeless" -> {
                    classes.add("net.minecraft.world.item.crafting.CraftingRecipe")
                    classes.add("net.minecraft.world.item.crafting.ShapelessRecipe")
                }
                "minecraft:crafting_special_armordye" -> classes.add("net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer")
            }

            classes
        }
    }

    @JvmStatic
    @JvmOverloads
    fun defineResourceType(rootFolder: String, path: String, extension: String = "", codecClasses: Set<String>) {
        return defineResourceType(rootFolder, path, extension) { element, json -> codecClasses }
    }

    @JvmStatic
    @JvmOverloads
    fun defineResourceType(rootFolder: String, path: String, extension: String = "", codecClasses: (element: JsonStringLiteral, json: JsonObject?) -> Set<String>) {
        KNOWN_CODECS[Regex("""$rootFolder\\$IDENTIFIER_REGEX+\\$path\\.+$extension""")] = { element, json, candidate ->
            val cls = PsiTreeUtil.getTopmostParentOfType(candidate, PsiClass::class.java)?.qualifiedName
            cls in codecClasses(element, json)
        }
    }

    override fun getReferencesByElement(
        element: PsiElement,
        context: ProcessingContext
    ): Array<out PsiReference?> {
        return if (element is JsonStringLiteral) arrayOf(
            element.containingFile.virtualFile?.let { file ->
                ProjectRootManager.getInstance(element.project).fileIndex.getSourceRootForFile(file)?.let { root ->
                    VfsUtilCore.getRelativePath(element.containingFile.virtualFile, root, '/')?.replace('/', '\\')?.let { path ->
                        KNOWN_CODECS.filter {
                            it.key.matches(path)
                        }.values.firstOrNull()?.let { predicate ->
                            val json = PsiTreeUtil.getTopmostParentOfType(element, JsonObject::class.java)
                            Reference(element) { candidate -> predicate(element, json, candidate) }
                        }
                    }
                }
            } ?: Reference(element)) else arrayOf()
    }

    fun getCodecFieldReferences(project: Project): List<PsiMethodCallExpression> = CachedValuesManager.getManager(project).getCachedValue(
        project,
        CACHE_KEY,
        {
            val facade = JavaPsiFacade.getInstance(project)
            val scope = GlobalSearchScope.allScope(project)
            val classes = CODEC_CLASSES.mapNotNull { facade.findClass(it, scope) }
            val methods = classes.flatMap { it.allMethods.filter { method -> method.name == "fieldOf" || method.name == "optionalFieldOf" } }.distinct()

            val calls = mutableListOf<PsiMethodCallExpression>()

            for (method in methods) {
                MethodReferencesSearch.search(
                    method,
                    scope,
                    false
                ).forEach { reference ->
                    PsiTreeUtil.getParentOfType(
                        reference.element,
                        PsiMethodCallExpression::class.java
                    )?.let(calls::add)
                }
            }

            CachedValueProvider.Result.create(
                calls,
                PsiModificationTracker.MODIFICATION_COUNT
            )
        },
        false
    )

    class Reference @JvmOverloads constructor(
        element: JsonStringLiteral,
        @JvmField
        val predicate: (candidate: PsiMethodCallExpression) -> Boolean = { true }
    ) : PsiReferenceBase<JsonStringLiteral>(element), PsiPolyVariantReference {
        override fun resolve(): PsiElement? {
            val multi = multiResolve(false)
            return if (multi.size == 1) multi[0].element else null
        }

        override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {
            return getCodecFieldReferences(element.project).mapNotNull { call ->
                val literal = call.argumentList.expressions.firstOrNull() as? PsiLiteralExpression ?: return@mapNotNull null

                if (literal.value != element.value) {
                    return@mapNotNull null
                }

                if (!predicate(call)) {
                    return@mapNotNull null
                }

                PsiElementResolveResult(ResolveTarget(literal, "${PsiTreeUtil.getParentOfType(call, PsiClass::class.java)?.qualifiedName ?: "Unknown"} ${call.text.split('\n').fold("") { left, right -> left + right.trim() }}"))
            }.toTypedArray()
        }
    }

    class ResolveTarget(
        private val target: PsiElement,
        @JvmField
        val name: String
    ) : PsiElement by target, NavigationItem {
        override fun getTextRangeInParent(): TextRange {
            return target.textRangeInParent
        }

        override fun getOwnDeclarations(): @Unmodifiable Collection<out PsiSymbolDeclaration> {
            return target.getOwnDeclarations()
        }

        override fun getOwnReferences(): @Unmodifiable Collection<out PsiSymbolReference> {
            return target.getOwnReferences()
        }

        override fun getName() = name

        override fun getPresentation() = object : ItemPresentation {
            override fun getPresentableText() = name

            override fun getIcon(unused: Boolean) = null
        }
    }
}