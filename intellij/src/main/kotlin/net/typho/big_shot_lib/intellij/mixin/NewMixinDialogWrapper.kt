package net.typho.big_shot_lib.intellij.mixin

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class NewMixinDialogWrapper(
    project: Project?
) : DialogWrapper(project) {
    init {
        init()
        title = "New Mixin"
    }

    override fun createCenterPanel(): JComponent? {
        return null
    }
}