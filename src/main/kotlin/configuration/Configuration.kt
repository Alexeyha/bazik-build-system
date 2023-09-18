package configuration

interface Configuration {
    fun getTargetTask() : String

    fun getLevelAndFileLogger() : Pair<String?, String?>

    fun isGraphRender() : Boolean

    fun getGraphFile() : String?
}
