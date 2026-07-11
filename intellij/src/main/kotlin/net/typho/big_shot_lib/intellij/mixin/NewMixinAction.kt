package net.typho.big_shot_lib.intellij.mixin

import com.demonwav.mcdev.asset.MixinAssets
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.PsiDirectory
import net.typho.big_shot_lib.intellij.BigShotLibBundle
import org.jetbrains.annotations.NonNls

class NewMixinAction : CreateFileFromTemplateAction() {
    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder.setTitle(BigShotLibBundle.message("action.NewMixinAction.dialog.title"))
            .addKind(BigShotLibBundle.message("action.NewMixinAction.mixin"), MixinAssets.MIXIN_CLASS_ICON, "Mixin")
            .addKind(BigShotLibBundle.message("action.NewMixinAction.accessor"), MixinAssets.MIXIN_ACCESSOR_ICON, "Accessor")
    }

    override fun getActionName(
        directory: PsiDirectory?,
        newName: @NonNls String,
        templateName: @NonNls String?
    ): @NlsContexts.Command String {
        return BigShotLibBundle.message("action.NewMixinAction.text")
    }
}