package net.typho.big_shot_lib.intellij.codecs

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
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
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.annotations.Unmodifiable

object CodecLookupReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(
        element: PsiElement,
        context: ProcessingContext
    ): Array<out PsiReference?> {
        return if (element is JsonStringLiteral) arrayOf(Reference(element)) else arrayOf()
    }

    class Reference(
        element: JsonStringLiteral
    ) : PsiReferenceBase<JsonStringLiteral>(element), PsiPolyVariantReference {
        companion object {
            @JvmField
            val CACHE_KEY = Key.create<CachedValue<List<PsiMethodCallExpression>>>("neo_codec_field_references")
        }

        override fun resolve(): PsiElement? {
            val multi = multiResolve(false)
            return if (multi.size == 1) multi[0].element else null
        }

        private fun getCodecReferences(
            project: Project,
            scope: GlobalSearchScope
        ): List<PsiMethodCallExpression> {
            return CachedValuesManager.getManager(project)
                .getCachedValue(
                    project,
                    CACHE_KEY,
                    {
                        val facade = JavaPsiFacade.getInstance(project)

                        val classes = listOfNotNull(
                            facade.findClass(
                                "com.mojang.serialization.Encoder",
                                scope
                            ),
                            facade.findClass(
                                "com.mojang.serialization.Decoder",
                                scope
                            ),
                            facade.findClass(
                                "com.mojang.serialization.MapCodec",
                                scope
                            )
                        )

                        val methods = classes.flatMap {
                            it.allMethods.filter { method ->
                                method.name == "fieldOf" ||
                                        method.name == "optionalFieldOf"
                            }
                        }.distinct()

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
        }

        override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {
            val start = System.currentTimeMillis()

            val key = element.value
            val project = element.project
            val scope = GlobalSearchScope.allScope(project)

            val results = mutableListOf<ResolveResult>()

            val calls = getCodecReferences(
                project,
                scope
            )

            println("search ${System.currentTimeMillis() - start}")

            for (call in calls) {
                val literal = call.argumentList
                    .expressions
                    .firstOrNull() as? PsiLiteralExpression
                    ?: continue

                if (literal.value != key) {
                    continue
                }

                val cls = PsiTreeUtil.getParentOfType(
                    call,
                    PsiClass::class.java
                )

                results += PsiElementResolveResult(
                    ResolveTarget(
                        literal,
                        "${cls?.name ?: "Unknown"} ${call.text}"
                    )
                )
            }

            println("done ${System.currentTimeMillis() - start}")

            return results.toTypedArray()
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