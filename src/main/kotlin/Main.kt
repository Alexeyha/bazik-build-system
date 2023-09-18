import configuration.ConfigurationJson
import graph.GraphNodes
import graph.builder.GraphBuilderImpl
import logger.Logger
import parser.ParserJson
import runner.Runner
import task.executor.TaskExecutorImpl

fun main() {
    try {
        val configuration = ConfigurationJson("conf.json")
        val parser = ParserJson()
        val graphBuilder = GraphBuilderImpl()
        val graph = GraphNodes()
        val taskExecutor = TaskExecutorImpl()
        Logger.get().setFileAndLevel(levelStr = configuration.getLevelAndFileLogger().first,
            logFilePath = configuration.getLevelAndFileLogger().second)

        val runner = Runner(parser, graphBuilder, graph, taskExecutor)
        
        runner.parseTarget(configuration.getTargetTask())
        runner.buildGraphOfTasks()
        if (configuration.isGraphRender()) {
            runner.renderEntireGraph(configuration.getGraphFile()!!)
        }
        runner.executeTasks()
    } catch (e : Exception) {
        println("Exception : ${e.message}")
    }
}
