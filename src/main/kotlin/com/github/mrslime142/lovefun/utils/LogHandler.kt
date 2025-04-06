package com.github.mrslime142.lovefun.utils

import com.github.mrslime142.lovefun.LoveFun.Companion.launch
import com.github.mrslime142.lovefun.modules.LoveFunModule
import com.github.mrslime142.lovefun.utils.ChatUtils.debug
import com.github.mrslime142.lovefun.utils.ChatUtils.getCurrentDateTimeString
import com.github.mrslime142.lovefun.utils.ChatUtils.modMessage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.Logger as javaLogger
import java.util.logging.SimpleFormatter
import java.util.zip.GZIPOutputStream

/**
 * im pretty sure this is bad but i could not figure out making log4j actually log to the right folder
 */
object LogHandler {
    object Logger {
        val log: javaLogger = javaLogger.getLogger("LoveFunLogger")

        fun info(message: String?) = log.info(message).also { debug(message) }

        fun warning(message: String?) = log.warning(message).also { debug(message) }

        fun severe(message: String?) = log.warning(message).also { debug(message) }

        fun fine(message: String?) = log.fine(message).also { debug(message) }

        fun message(message: String, data: Any? = null) = log.info("Recieved message: $message." + (data?.let { " With data: $it" } ?: "")).also { debug("$message." + (data?.let { " With data: $it" } ?: "")) }
    }

    init {
        compressOldLogs()
        setupLogger()
    }

    private fun compressOldLogs() = launch {
        val logDir = File("config/lovefun/mod_logs")
        if (!logDir.exists() || !logDir.isDirectory) return@launch
        val logFiles = logDir.listFiles { file -> file.isFile && file.name.endsWith(".log") }
        logFiles?.forEach { file ->
            val tarGzFileName = "${file.absolutePath}.tar.gz"
            compressFileToTarGz(file, tarGzFileName)
            file.delete()
        }
    }

    private fun compressFileToTarGz(file: File, tarGzFileName: String) = runCatching {
        FileOutputStream(tarGzFileName).use { output ->
            GZIPOutputStream(output).use { gzipOut ->
                FileInputStream(file).use { input ->
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (input.read(buffer).also { len = it } >= 0) {
                        gzipOut.write(buffer, 0, len)
                    }
                }
            }
        }
    }.onFailure { it.printStackTrace() }

    private fun setupLogger() = runCatching {
        val logDir = File("config/lovefun/mod_logs")
        if (!logDir.exists()) logDir.mkdirs()

        val fileHandler = FileHandler("${logDir.absolutePath}/${SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Date())}.log", true)
        fileHandler.formatter = SimpleFormatter()
        Logger.log.addHandler(fileHandler)
        Logger.log.useParentHandlers = false
    }.onFailure { it.printStackTrace() }

    fun logJsonToFile(jsonString: String, name: String = "unknown", type: String = "unknown", dir: String = "hypixel_logs") {
        if (!LoveFunModule.enabled || !LoveFunModule.logJson) return
        val logFolder = File(File("config/lovefun"), dir)
        if (!logFolder.exists()) logFolder.mkdirs()
        File(logFolder, "${name}_${type}_${getCurrentDateTimeString()}.json").writeText(jsonString)
    }
}