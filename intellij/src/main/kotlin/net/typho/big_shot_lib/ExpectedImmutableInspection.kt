package net.typho.big_shot_lib

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiType
import com.intellij.uast.UastHintedVisitorAdapter
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.UVariable
import org.jetbrains.uast.toUElement
import org.jetbrains.uast.visitor.AbstractUastNonRecursiveVisitor
import kotlin.jvm.java
import kotlin.math.min

class ExpectedImmutableInspection : LocalInspectionTool() {
    private val immutableAnnotations = mutableSetOf(
        "net.typho.big_shot_lib.api.plugin.Immutable"
    )
    private val mutableAnnotations = mutableSetOf(
        "net.typho.big_shot_lib.api.plugin.MaybeMutable",
        "net.typho.big_shot_lib.api.plugin.Mutable"
    )

    class Fix : LocalQuickFix {
        override fun getFamilyName() = BigShotLibBundle.message("inspection.messages.expected_immutable.references.use.quickfix")

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val psi = descriptor.psiElement

            WriteCommandAction.runWriteCommandAction(project) {
                psi.replace(PsiElementFactory.getInstance(project).createExpressionFromText("${psi.text}.immutable()", psi))
            }
        }
    }

    private fun isMutable(expr: UExpression): Boolean {
        if (isMutable(expr.getExpressionType())) {
            return true
        }

        return when (expr) {
            is USimpleNameReferenceExpression -> {
                when (val element = expr.resolve()?.toUElement()) {
                    is UVariable -> {
                        element.uastInitializer?.let {
                            isMutable(it)
                        } ?: false
                    }

                    else -> false
                }
            }

            is UCallExpression -> {
                isMutable(expr.getExpressionType())
            }

            is UQualifiedReferenceExpression -> {
                isMutable(expr.selector)
            }

            else -> false
        }
    }

    private fun isMutable(type: PsiType?): Boolean {
        val classType = type as? PsiClassType ?: return false

        if (type.annotations.any { it.qualifiedName in immutableAnnotations }) {
            return false
        }

        if (type.annotations.any { it.qualifiedName in mutableAnnotations }) {
            return true
        }

        val psiClass = classType.resolve() ?: return false
        val uClass = psiClass.toUElement() as? UClass

        if (immutableAnnotations.any { uClass?.findAnnotation(it) != null }) {
            return false
        }

        if (mutableAnnotations.any { uClass?.findAnnotation(it) != null }) {
            return true
        }

        return psiClass.superTypes.any {
            isMutable(it)
        }
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return UastHintedVisitorAdapter.create(
            holder.file.language,
            object : AbstractUastNonRecursiveVisitor() {
                override fun visitCallExpression(node: UCallExpression): Boolean {
                    val method = node.resolve() ?: return super.visitCallExpression(node)

                    val params = method.parameterList.parameters
                    val args = node.valueArguments

                    repeat(min(params.size, args.size)) { i ->
                        val param = params[i]
                        val arg = args[i]

                        if (param.type.annotations.any { it.qualifiedName in immutableAnnotations }) {
                            if (isMutable(arg)) {
                                holder.registerProblem(
                                    arg.sourcePsi ?: return@repeat,
                                    BigShotLibBundle.message("inspection.messages.expected_immutable.references.problem.descriptor"),
                                    Fix()
                                )
                            }
                        }
                    }

                    return super.visitCallExpression(node)
                }
            },
            arrayOf(UCallExpression::class.java),
            true
        )
    }
}