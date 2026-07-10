package net.typho.big_shot_lib

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.uast.UastHintedVisitorAdapter
import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.skipParenthesizedExprDown
import org.jetbrains.uast.toUElement
import org.jetbrains.uast.visitor.AbstractUastNonRecursiveVisitor
import kotlin.jvm.java
import kotlin.math.min

class ExpectedImmutableInspection : LocalInspectionTool() {
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

                        if (param.hasAnnotation("net.typho.big_shot_lib.api.plugin.Immutable") && arg.getExpressionType()?.annotations?.any { it.qualifiedName == "net.typho.big_shot_lib.api.plugin.MaybeMutable" } == true) {
                            holder.registerProblem(
                                arg.sourcePsi ?: return@repeat,
                                BigShotLibBundle.message("inspection.messages.expected_immutable.references.problem.descriptor")
                                // TODO quick fix
                            )
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