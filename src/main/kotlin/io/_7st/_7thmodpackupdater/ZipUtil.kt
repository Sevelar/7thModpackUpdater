package io._7st._7thmodpackupdater

import java.nio.file.Path
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.exception.ZipException

class ZipUtil {
    companion object {
        fun unzipFileTo(sourcePath: Path, destinationPath: Path) {
            try {
                val zipFile = ZipFile(sourcePath.toFile())
                zipFile.extractAll(destinationPath.toString())
            } catch (error: ZipException) {
                error.printStackTrace()
            }
        }
    }
}