package io._7st._7thmodpackupdater

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class JavaFXApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(JavaFXApplication::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 720.0, 240.0)

        val mainController = fxmlLoader.getController<MainController>()
        mainController.execute(stage)

        stage.scene = scene
        stage.title = "7th Guild MC Modpack Updater"
        stage.isResizable = false
        stage.show()
    }

    companion object {
        @JvmStatic
        fun main() {
            launch(JavaFXApplication::class.java)
        }
    }
}

