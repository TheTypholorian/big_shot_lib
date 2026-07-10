package net.typho.big_shot_lib.intellij.services

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.json.psi.JsonArray
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonStringLiteral
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import net.typho.big_shot_lib.intellij.BigShotLibBundle

class InvalidNeoServiceInspection : LocalInspectionTool() {
    private fun checkImplementation(
        apiClass: PsiClass,
        implName: String,
        element: JsonStringLiteral,
        holder: ProblemsHolder
    ) {
        val implClass = JavaPsiFacade.getInstance(element.project).findClass(implName, element.resolveScope)

        if (implClass == null) {
            holder.registerProblem(
                element,
                BigShotLibBundle.message("inspection.messages.invalid_service.nonexistent.references.problem.descriptor", implName)
            )

            return
        }

        if (!implClass.isInheritor(apiClass, true)) {
            holder.registerProblem(
                element,
                BigShotLibBundle.message("inspection.messages.invalid_service.inheritance.references.problem.descriptor", implName, apiClass.qualifiedName)
            )
        }
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitFile(file: PsiFile) {
                if (file is JsonFile && file.name == "neo_services.json") {
                    val root = file.topLevelValue

                    if (root is JsonObject) {
                        root.propertyList.forEach { property ->
                            val serviceName = property.name
                            val serviceClass = JavaPsiFacade.getInstance(property.project)
                                .findClass(serviceName, property.resolveScope)

                            if (serviceClass == null) {
                                holder.registerProblem(
                                    property,
                                    BigShotLibBundle.message("inspection.messages.invalid_service.nonexistent.references.problem.descriptor", serviceName)
                                )
                                return
                            }

                            when (val value = property.value) {
                                is JsonStringLiteral -> checkImplementation(serviceClass, value.value, value, holder)

                                is JsonArray -> value.valueList.forEach {
                                    if (it is JsonStringLiteral) {
                                        checkImplementation(serviceClass, it.value, it, holder)
                                    }
                                }

                                else -> holder.registerProblem(
                                    property,
                                    "Implementation must be a class name or array of class names"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}