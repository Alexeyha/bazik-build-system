import graph.GraphNodes
import graph.builder.GraphBuilderImpl
import logger.Logger
import parser.ParserJson
import runner.Runner
import task.executor.TaskExecutorImpl

fun main() {
    val parser = ParserJson()
    val graphBuilder = GraphBuilderImpl()
    val graph = GraphNodes()
    val taskExecutor = TaskExecutorImpl()
    Logger.get().setFileAndLevel(levelStr = "debug")
    val runner = Runner(parser, graphBuilder, graph, taskExecutor)
    runner.parseTarget("build_1.json:run_1")
    runner.buildGraphOfTasks()
    runner.renderEntireGraph("graph.txt")
    runner.executeTasks()
}
