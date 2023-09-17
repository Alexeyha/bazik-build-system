import graph.GraphNodes
import graph.builder.GraphBuilderImpl
import logger.Logger
import parser.JsonParser
import runner.Runner
import task.executor.TaskExecutorImpl

//val task_compile = Task(
//    name = "compile",
//    action = {
//        println("compile")
//    }
//)
//
//val task_build = Task(
//    name = "build",
//    action = {
//        println("build")
//    }
//)
//
//val tasks = listOf(
//    task_compile,
//    task_build
//)

fun main() {
//    val nameToTask: Map<String, Task> = tasks.associateBy { task -> task.name }

    val parser = JsonParser()
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
