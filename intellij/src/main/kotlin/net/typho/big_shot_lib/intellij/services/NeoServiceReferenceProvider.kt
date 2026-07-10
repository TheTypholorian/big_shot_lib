package net.typho.big_shot_lib.intellij.services

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

object NeoServiceReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(
        element: PsiElement,
        context: ProcessingContext
    ): Array<out PsiReference?> {
        return if (element is JsonStringLiteral) arrayOf(Reference(element)) else arrayOf()
    }

    class Reference(
        element: JsonStringLiteral
    ) : PsiReferenceBase<JsonStringLiteral>(element) {
        override fun resolve(): PsiElement? {
            val path = element.value.split('$')

            if (path.isEmpty()) {
                return null
            }

            var cls = JavaPsiFacade.getInstance(element.project).findClass(path[0], element.resolveScope) ?: return null

            if (cls.parent is PsiClass) {
                return null
            }

            repeat(path.size - 1) {
                cls = cls.findInnerClassByName(path[it + 1], false) ?: return null
            }

            return cls
        }
    }
}