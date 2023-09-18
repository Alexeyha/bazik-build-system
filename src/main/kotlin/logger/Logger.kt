package logger

import java.io.File

class Logger private constructor() {
    private var file: File? = null
    private var level: LogLevel = LogLevel.NOTHING

    companion object {
        private var instance: Logger? = null

        fun get(): Logger {
            if (instance == null) {
                instance = Logger()
            }
            return instance!!
        }
    }

    fun setFileAndLevel(logFilePath: String? = null, levelStr: String? = null) {
        file = logFilePath?.let { File(it) }
        if (levelStr != null) {
            level = when (levelStr) {
                "debug" -> LogLevel.DEBUG
                "info" -> LogLevel.INFO
                "warning" -> LogLevel.WARNING
                "error" -> LogLevel.ERROR
                else -> throw IllegalArgumentException("Incorrect option log level: $levelStr")
            }
        }
        if (file != null && (!file!!.exists() || !file!!.isFile)) {
            throw IllegalArgumentException("Log file on the path $logFilePath does not exist")
        }
    }

    private enum class LogLevel {
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        NOTHING
    }

    private fun log(logLevel: LogLevel, message: String) {
        if (logLevel >= level) {
            if (file != null) {
                file!!.appendText("[$logLevel] - $message\n")
            } else {
                println("[$logLevel] - $message")
            }
        }
    }
    fun debug(message: String) {
        log(LogLevel.DEBUG, message)
    }

    fun info(message: String) {
        log(LogLevel.INFO, message)
    }

    fun warning(message: String) {
        log(LogLevel.WARNING, message)
    }

    fun error(message: String) {
        log(LogLevel.ERROR, message)
    }
}
