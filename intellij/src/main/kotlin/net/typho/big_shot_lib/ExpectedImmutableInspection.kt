package net.typho.big_shot_lib

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiElementVisitor
import com.intellij.uast.UastHintedVisitorAdapter
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.visitor.AbstractUastNonRecursiveVisitor
import kotlin.jvm.java
import kotlin.math.min

class ExpectedImmutableInspection : LocalInspectionTool() {
    class Fix : LocalQuickFix {
        override fun getFamilyName() = BigShotLibBundle.message("inspection.messages.expected_immutable.references.use.quickfix")

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val psi = descriptor.psiElement

            WriteCommandAction.runWriteCommandAction(project) {
                psi.replace(PsiElementFactory.getInstance(project).createExpressionFromText("${psi.text}.immutable()", psi))
            }
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
                        if (params[i].type.annotations.any { it.qualifiedName in MutabilityState.immutableAnnotations }) {
                            val arg = args[i]

                            if (MutabilityState.get(arg) != MutabilityState.IMMUTABLE) {
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