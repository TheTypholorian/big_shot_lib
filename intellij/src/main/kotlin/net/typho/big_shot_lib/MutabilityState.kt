package net.typho.big_shot_lib

import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UVariable
import org.jetbrains.uast.toUElement
import kotlin.collections.contains

enum class MutabilityState {
    MUTABLE,
    MAYBE_MUTABLE,
    IMMUTABLE;

    companion object {
        const val IMMUTABLE_ANNOTATION = "net.typho.big_shot_lib.api.plugin.Immutable"
        const val MAYBE_MUTABLE_ANNOTATION = "net.typho.big_shot_lib.api.plugin.MaybeMutable"
        const val MUTABLE_ANNOTATION = "net.typho.big_shot_lib.api.plugin.Mutable"

        // TODO configurable
        @JvmField
        val immutableAnnotations = mutableSetOf(IMMUTABLE_ANNOTATION)
        @JvmField
        val maybeMutableAnnotations = mutableSetOf(MAYBE_MUTABLE_ANNOTATION)
        @JvmField
        val mutableAnnotations = mutableSetOf(MUTABLE_ANNOTATION)

        @JvmStatic
        fun get(expression: UExpression): MutabilityState? {
            expression.getExpressionType()?.let { getLocalDefined(it)?.let { return it } }

            return when (expression) {
                is USimpleNameReferenceExpression -> {
                    val element = expression.resolve()?.toUElement()
                    if (element is UVariable) element.uastInitializer?.let { get(it) } else null
                }

                is UCallExpression -> expression.getExpressionType()?.let { getLocalDefined(it) }
                is UQualifiedReferenceExpression -> get(expression.selector)
                else -> null
            } ?: expression.getExpressionType()?.let { getClassDefined(it) }
        }

        @JvmStatic
        fun get(type: PsiType): MutabilityState? {
            return getLocalDefined(type) ?: getClassDefined(type)
        }

        @JvmStatic
        fun getLocalDefined(type: PsiType): MutabilityState? {
            if (type.annotations.any { it.qualifiedName in immutableAnnotations }) {
                return IMMUTABLE
            }

            if (type.annotations.any { it.qualifiedName in maybeMutableAnnotations }) {
                return MAYBE_MUTABLE
            }

            if (type.annotations.any { it.qualifiedName in mutableAnnotations }) {
                return MUTABLE
            }

            return null
        }

        @JvmStatic
        fun getClassDefined(type: PsiType?): MutabilityState? {
            val psiClass = (type as? PsiClassType)?.resolve() ?: return null
            val uClass = psiClass.toUElement() as? UClass

            if (immutableAnnotations.any { uClass?.findAnnotation(it) != null }) {
                return IMMUTABLE
            }

            if (maybeMutableAnnotations.any { uClass?.findAnnotation(it) != null }) {
                return MAYBE_MUTABLE
            }

            if (mutableAnnotations.any { uClass?.findAnnotation(it) != null }) {
                return MUTABLE
            }

            return psiClass.superTypes.firstNotNullOfOrNull { getClassDefined(it) }
        }
    }
}