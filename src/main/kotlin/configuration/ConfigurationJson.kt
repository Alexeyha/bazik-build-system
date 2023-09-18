package configuration

import java.io.File
import java.lang.IllegalArgumentException
import org.json.JSONObject

class ConfigurationJson(private val filePath: String) : Configuration {
    private var targetTask: String = ""
    private var levelAndFile: Pair<String?, String?> = Pair(null, null)
    private var isGraphRender = false
    private var graphFile: String? = null

    init {
        parse()
    }
    override fun getTargetTask(): String {
        return targetTask
    }

    override fun getLevelAndFileLogger(): Pair<String?, String?> {
        return levelAndFile
    }

    override fun isGraphRender(): Boolean {
        return isGraphRender
    }

    override fun getGraphFile(): String? {
        return graphFile
    }

    private fun parse() {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("Configuration (conf.json) file does not exist in the path: $filePath")
        }
        val json = JSONObject(file.readText())
        targetTask = json.getString("target_task")
        val jsonGraph = json.getJSONObject("graph")
        isGraphRender = jsonGraph.getBoolean("render")
        if (isGraphRender) graphFile = jsonGraph.getString("file")
        val jsonLogger = json.getJSONObject("logger")
        val isLog = jsonLogger.getBoolean("log")
        if (isLog) {
            val logLevel = jsonLogger.getString("level")
            val logFile = if (jsonLogger.has("file")) jsonLogger.getString("file") else null
            levelAndFile = Pair(logLevel, logFile)
        }
    }

}
