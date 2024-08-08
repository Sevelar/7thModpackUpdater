package io._7st._7thmodpackupdater

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class URLUtil {
    companion object {
        private const val HTTP_TIMEOUT_MS = 8_000

        private fun openUrl(url: URL): InputStream? {
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = HTTP_TIMEOUT_MS
            connection.readTimeout = HTTP_TIMEOUT_MS
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode < 200 || responseCode >= 300) {
                throw IOException("HTTP request to $url failed: $responseCode")
            }

            return connection.inputStream
        }

        fun downloadFileTo(url: URL, path: Path) {
            try {
                openUrl(url).use { `in` ->
                    if (`in` != null) {
                        Files.copy(`in`, path, StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            } catch (t: Throwable) {
                throw t
            }
        }
    }
}

