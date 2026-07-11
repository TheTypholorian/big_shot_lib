package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.platform.mixin.util.isAccessorMixin
import com.demonwav.mcdev.platform.mixin.util.mixinTargets
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import com.intellij.psi.PsiTypeElement
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import java.util.function.BiConsumer
import java.util.function.Predicate

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
}