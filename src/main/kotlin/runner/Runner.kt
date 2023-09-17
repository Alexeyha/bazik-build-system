package runner

import graph.Graph
import graph.builder.GraphBuilder
import parser.Parser
import task.Task
import task.executor.TaskExecutor
import logger.Logger
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet

class Runner(private val parser: Parser, private val graphBuilder: GraphBuilder,
             private val graph: Graph, private val taskExecutor: TaskExecutor) {

    private val usedBuildFilesSet: TreeSet<String> = TreeSet()
    private val filesQueue: Queue<String> = LinkedList()

    private var tasks = mapOf<String, Task>()
    private var targetTask: String? = null

    fun parseTarget(fullPathTask: String) {
        Logger.get().debug("Start parseTarget")
        val tasksList = mutableListOf<Task>()

        if (!fullPathTask.contains(":")) {
            throw IllegalArgumentException("Task's full path must contain ':' symbol")
        }
        val (pathName, taskName) = fullPathTask.split(":")

        filesQueue.add(pathName)
        while (filesQueue.isNotEmpty()) {
            val curPath = filesQueue.remove()
            if (usedBuildFilesSet.contains(curPath)) continue
            usedBuildFilesSet.add(curPath)
            val (curTasks, filesSet) = parser.parse(curPath)
            tasksList.addAll(curTasks)
            filesQueue.addAll(filesSet)
        }
        tasks = tasksList.associateBy { task -> task.name }
        targetTask = taskName
    }

    fun buildGraphOfTasks() {
        Logger.get().debug("Start buildGraphOfTask")
        graphBuilder.buildGraph(graph, tasks)
    }

    fun executeTasks() {
        if (targetTask == null) throw IllegalStateException("Target task is null, try to run parseTarget")
        if (!tasks.containsKey(targetTask)) throw IllegalStateException("Target task $targetTask not found")
        val tasksQueue = graph.buildQueueOfTasks(tasks[targetTask]!!)
        taskExecutor.executeTasks(tasksQueue)
    }

    fun renderEntireGraph(filePath: String) {
        graph.renderEntireGraphInFile(filePath)
    }
}
