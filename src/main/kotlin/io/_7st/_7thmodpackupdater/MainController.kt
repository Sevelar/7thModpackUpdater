package io._7st._7thmodpackupdater

import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import org.apache.commons.io.FileUtils
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.text.DecimalFormat
import java.text.ParsePosition
import java.util.function.UnaryOperator
import java.util.regex.Pattern
import kotlin.io.path.exists
import kotlin.io.path.notExists

private val JAVA_MAX_ALLOC_REGEX = "-Xmx\\d+G".toRegex()

class MainController {

    private lateinit var stage: Stage

    private var minecraftPath: Path? = null

    @FXML
    private lateinit var minecraftPathField: TextField

    @FXML
    private lateinit var javaMaxMemAllocField: TextField

    fun execute(_stage: Stage) {
        stage = _stage
        try {
            setMinecraftPath()
            setJavaMaxAllocFieldProps()
        } catch (error: Throwable) {
            DialogController.showDialog(error.toString())
        }
    }

    private fun setMinecraftPath() {
        minecraftPathField.isEditable = false

        val defaultPath = Path.of(System.getProperty("user.home") + "\\AppData\\Roaming\\.minecraft")
        if (defaultPath.exists()) {
            minecraftPath = defaultPath
            minecraftPathField.text = defaultPath.toString()
        }
    }

    private fun setJavaMaxAllocFieldProps() {
        val format = DecimalFormat( "#.0" )

        javaMaxMemAllocField.textFormatter =
            TextFormatter<UnaryOperator<TextFormatter.Change?>>(UnaryOperator { c: TextFormatter.Change? ->
                if (c!!.controlNewText.isEmpty()) {
                    return@UnaryOperator c
                }
                val parsePosition = ParsePosition(0)
                val `object` = format.parse(c.controlNewText, parsePosition)
                if (`object` == null || parsePosition.index < c.controlNewText.length) {
                    return@UnaryOperator null
                } else {
                    return@UnaryOperator c
                }
            })
    }

    @FXML
    private fun onChooseDirectory() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.initialDirectory = if (minecraftPath != null) {
            minecraftPath!!.toFile()
        } else {
            Path.of(System.getProperty("user.home")).toFile()
        }

        val directory = directoryChooser.showDialog(stage)
        if (directory != null) {
            minecraftPath = Path.of(directory.path)
            minecraftPathField.text = directory.path
        }
    }

    @FXML
    private fun onJavaMaxAlloc() {
        if (minecraftPath == null) {
            throw Exception("Please choose a valid .minecraft directory before proceeding.")
        }

        val profileConfigPath = Path.of(minecraftPath.toString(), "launcher_profiles.json")
        if (profileConfigPath.notExists()) {
            throw Exception("The 'launcher_profiles.json' has not been found. Please verify your .minecraft path and. Non-vanilla launchers are not supported.")
        }

        try {
            val parsedProfileConfig = JSONUtil.readFromJson(profileConfigPath)
            val fabricProfile = parsedProfileConfig.profiles["fabric-loader-1.20.1"]

            if (fabricProfile != null) {
                if (fabricProfile.javaArgs != null) {
                    val stringPattern = Pattern.compile(JAVA_MAX_ALLOC_REGEX.toString())
                    val matcher = stringPattern.matcher(fabricProfile.javaArgs!!)

                    if (matcher.find()) {
                        fabricProfile.javaArgs = fabricProfile.javaArgs!!.replace(JAVA_MAX_ALLOC_REGEX, "-Xmx${javaMaxMemAllocField.text}G")
                    } else {
                        fabricProfile.javaArgs += " -Xmx${javaMaxMemAllocField.text}G"
                    }
                } else {
                    fabricProfile.javaArgs = "-Xmx${javaMaxMemAllocField.text}G"
                }

                JSONUtil.writeToFile(profileConfigPath, parsedProfileConfig)
                DialogController.showDialog("Maximum memory allocation has been successfully updated!")
            } else {
                throw Exception("The valid 'fabric-loader-1.20.1' profile has not been found. This error might have also occurred, because you renamed it.")
            }
        } catch (error: Throwable) {
            error.printStackTrace()
            throw Exception("An unknown error has occurred!")
        }
    }

    @FXML
    private fun onUpdateModpack() {
        val tempDirectoryPath = Files.createTempDirectory(null)
        val modpackFilePath = Path.of(tempDirectoryPath.toString(), "the-7th-guild-modpack.zip")
        val modsFolderPath = Path.of(minecraftPath.toString(), "mods")

        try {
            if (modsFolderPath.exists()) {
                val timestamp = System.currentTimeMillis() / 1000L
                val modsBackupPath = Path.of(minecraftPath.toString(), "mods-${timestamp}")
                Files.move(modsFolderPath, modsBackupPath)
                Files.createDirectory(modsFolderPath)
            } else {
                Files.createDirectory(modsFolderPath)
            }

            URLUtil.downloadFileTo(URL("https://7st.io/static/modpack"), modpackFilePath)
            ZipUtil.unzipFileTo(modpackFilePath, modsFolderPath)

            DialogController.showDialog("The modpack has been successfully updated!")
        } catch (error: Throwable) {
            error.printStackTrace()
            throw Exception("An error has occurred during update.")
        } finally {
            try {
                Files.deleteIfExists(modpackFilePath)
                Files.deleteIfExists(tempDirectoryPath)
            } catch (error: Throwable) {
                error.printStackTrace()
                throw Exception("An error has occurred while removing temp files!")
            }
        }
    }
}