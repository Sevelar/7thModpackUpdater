package io._7st._7thmodpackupdater

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.nio.file.Path
import kotlin.io.path.reader
import kotlin.io.path.writer

class JSONUtil {
    companion object {
       private val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

        fun readFromJson(path: Path): MinecraftProfileConfiguration {
            val jsonReader = JsonReader(path.reader())
            val parsedProfileConfig = gson.fromJson<MinecraftProfileConfiguration>(jsonReader)
            jsonReader.close()

            return parsedProfileConfig
        }

        fun writeToFile(path: Path, data: MinecraftProfileConfiguration) {
            val outputWriter = path.writer()
            gson.toJson(data, outputWriter)
            outputWriter.close()
        }

        private inline fun <reified T> Gson.fromJson(jsonReader: JsonReader): T = fromJson(jsonReader, object: TypeToken<T>() {}.type)
    }
}