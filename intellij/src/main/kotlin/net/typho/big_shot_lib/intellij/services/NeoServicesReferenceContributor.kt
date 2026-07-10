package net.typho.big_shot_lib.intellij.services

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar
import kotlin.jvm.java

class NeoServicesReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(JsonStringLiteral::class.java).inVirtualFile(PlatformPatterns.virtualFile().withName("neo_services.json")),
            NeoServiceReferenceProvider
        )
    }
}