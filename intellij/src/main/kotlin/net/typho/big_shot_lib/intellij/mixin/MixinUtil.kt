package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.platform.mixin.MixinModule
import com.demonwav.mcdev.platform.mixin.util.isAccessorMixin
import com.demonwav.mcdev.platform.mixin.util.mixinTargets
import com.demonwav.mcdev.util.fullQualifiedName
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil

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
}