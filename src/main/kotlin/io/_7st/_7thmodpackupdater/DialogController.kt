package io._7st._7thmodpackupdater

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage

class DialogController {
    @FXML
    lateinit var dialogLabel: Label

    fun setDialogLabelText(text: String) {
        dialogLabel.text = text
    }

    companion object {
        fun showDialog(text: String) {
            val fxmlLoader = FXMLLoader(MainController::class.java.getResource("dialog.fxml"))
            val root = fxmlLoader.load<Parent>()

            val dialogController = fxmlLoader.getController<DialogController>()
            dialogController.setDialogLabelText(text)

            val _stage = Stage()
            _stage.scene = Scene(root, 420.0, 140.0)
            _stage.title = "7th Guild MC Modpack Updater"
            _stage.isResizable = false
            _stage.show()
        }
    }
}