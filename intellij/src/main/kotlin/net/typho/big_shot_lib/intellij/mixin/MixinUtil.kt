package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.platform.mixin.util.isAccessorMixin
import com.demonwav.mcdev.platform.mixin.util.mixinTargets
import com.demonwav.mcdev.util.descriptor
import com.demonwav.mcdev.util.psiType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.CompilerModuleExtension
import com.intellij.openapi.roots.OrderEnumerator
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.ClassFileViewProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLambdaExpression
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameterListOwner
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.PsiTypeElement
import com.intellij.psi.PsiTypes
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.TypeConversionUtil
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Handle
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodNode
import java.util.function.BiConsumer

object MixinUtil {
    @JvmStatic
    fun isInSource(fileIndex: ProjectFileIndex, file: VirtualFile): Boolean {
        if (!fileIndex.isInSource(file)) {
            return false
        }

        var current: VirtualFile? = file

        while (current != null) {
            if (fileIndex.isExcluded(current) || fileIndex.isInGeneratedSources(current) || fileIndex.isUnderIgnored(current)) {
                return false
            }

            current = current.parent
        }

        return true
    }

    @JvmStatic
    fun getAccessors(project: Project, scope: GlobalSearchScope, target: PsiClass): List<PsiClass> {
        val targetName = (target.qualifiedName ?: return listOf()).replace('.', '/')
        val fileIndex = ProjectFileIndex.getInstance(project)

        return getMixins(project, scope).filter { cls ->
            isInSource(fileIndex, cls.containingFile.virtualFile) && cls.isWritable && cls.isAccessorMixin && cls.mixinTargets.any { it.name == targetName }
        }
    }

    @JvmStatic
    fun getMixins(project: Project, scope: GlobalSearchScope, target: PsiClass): List<PsiClass> {
        val targetName = (target.qualifiedName ?: return listOf()).replace('.', '/')
        val fileIndex = ProjectFileIndex.getInstance(project)

        return getMixins(project, scope).filter { cls ->
            isInSource(fileIndex, cls.containingFile.virtualFile) && cls.isWritable && cls.mixinTargets.any { it.name == targetName }
        }
    }

    @JvmStatic
    fun getMixins(project: Project, scope: GlobalSearchScope): List<PsiClass> {
        return JavaAnnotationIndex.getInstance()
            .getAnnotations(
                "Mixin",
                project,
                scope
            )
            .filter { it.hasQualifiedName("org.spongepowered.asm.mixin.Mixin") }
            .mapNotNull { anno -> PsiTreeUtil.getParentOfType(anno, PsiClass::class.java) }
    }

    @JvmStatic
    fun findOrCreateMixin(project: Project, mixins: List<PsiClass>, target: PsiClass): PsiClass {
        return when (mixins.size) {
            0 -> TODO("make mixin")
            1 -> mixins[0]
            else -> TODO("prompt for which mixin")
        }
    }

    @JvmStatic
    fun findOrCreateMixin(project: Project, scope: GlobalSearchScope, target: PsiClass): PsiClass {
        return findOrCreateMixin(project, getMixins(project, scope, target), target)
    }

    @JvmStatic
    fun findOrCreateAccessor(project: Project, scope: GlobalSearchScope, target: PsiClass): PsiClass {
        return findOrCreateMixin(project, getAccessors(project, scope, target), target)
    }

    @JvmStatic
    fun findOrAddMixinMethod(project: Project, mixin: PsiClass, methodName: String, methodType: PsiType, methodInit: BiConsumer<PsiElementFactory, PsiMethod>): PsiMethod {
        val factory = PsiElementFactory.getInstance(project)

        var method = factory.createMethod(methodName, methodType)
        methodInit.accept(factory, method)

        mixin.findMethodBySignature(method, false)?.let { existing ->
            method = existing
        } ?: WriteCommandAction.runWriteCommandAction(project) {
            method = mixin.add(method) as PsiMethod
        }

        FileEditorManager.getInstance(project)
            .openTextEditor(OpenFileDescriptor(project, mixin.containingFile.virtualFile), true)
            ?.caretModel
            ?.moveToOffset(method.textRange.startOffset)

        return method
    }

    @JvmStatic
    fun findOrAddMixinMethod(project: Project, mixin: PsiClass, methodName: String, methodType: PsiTypeElement, methodInit: BiConsumer<PsiElementFactory, PsiMethod>): PsiMethod {
        val factory = PsiElementFactory.getInstance(project)

        var method = factory.createMethod(methodName, methodType.type)
        method.returnTypeElement?.replace(methodType)
        methodInit.accept(factory, method)

        mixin.findMethodBySignature(method, false)?.let { existing ->
            method = existing
        } ?: WriteCommandAction.runWriteCommandAction(project) {
            method = mixin.add(method) as PsiMethod
        }

        FileEditorManager.getInstance(project)
            .openTextEditor(OpenFileDescriptor(project, mixin.containingFile.virtualFile), true)
            ?.caretModel
            ?.moveToOffset(method.textRange.startOffset)

        return method
    }

    @JvmStatic
    fun injectImplementedByMixinException(project: Project, method: PsiMethod) {
        val factory = PsiElementFactory.getInstance(project)
        method.body?.add(factory.createStatementFromText(
            "throw new UnsupportedOperationException(\"Implemented via Mixin\");",
            method
        ))
    }

    @JvmStatic
    fun getAtInvokeMethodDescriptor(method: PsiMethod): String {
        return "${PsiTreeUtil.getParentOfType(method, PsiClass::class.java)?.let { TypeConversionUtil.erasure(it.psiType).descriptor } ?: ""}${method.name}(${method.parameterList.parameters.joinToString(separator = "") { TypeConversionUtil.erasure(it.type).descriptor }})${TypeConversionUtil.erasure(method.returnType ?: PsiTypes.voidType()).descriptor}"
    }

    @JvmStatic
    fun getInjectionTargetMethodDescriptor(element: PsiParameterListOwner, methodName: String, returnType: PsiType): String {
        val cls = PsiTreeUtil.getParentOfType(element, PsiClass::class.java) ?: return methodName
        val methods = cls.findMethodsByName(methodName, false)

        if (methods.isEmpty() || methods.size == 1) {
            return methodName
        }

        return "$methodName(${element.parameterList.parameters.joinToString(separator = "") { TypeConversionUtil.erasure(it.type).descriptor }})${TypeConversionUtil.erasure(returnType).descriptor}"
    }

    @JvmStatic
    fun getTypeNameForTypeParameter(project: Project, type: PsiType?) = (if (type is PsiPrimitiveType) type.getBoxedType(PsiManager.getInstance(project), GlobalSearchScope.allScope(project)) else type)?.canonicalText

    @JvmStatic
    fun <E : PsiElement> AnActionEvent.getSelectedPsiElement(cls: Class<E>): E? {
        val editor = getData(CommonDataKeys.EDITOR) ?: return null
        val file = getData(CommonDataKeys.PSI_FILE) ?: return null
        val element = file.findElementAt(editor.caretModel.offset) ?: return null
        return if (cls.isInstance(element)) cls.cast(element) else PsiTreeUtil.getParentOfType(element, cls, false)
    }

    /**
     * TODO figure out how to look up the actual lambda method name
     */
    @JvmStatic
    fun getLambdaMethodName(at: PsiLambdaExpression): String {
        val method = PsiTreeUtil.getParentOfType(at, PsiMethod::class.java)
        return if (method == null) "unknown_lambda_method" else "unknown_lambda_method_in_${method.name}"
    }
}