package runner

import graph.builder.GraphBuilder
import parser.Parser
import task.Task
import task.executor.TaskExecutor
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet
import java.util.logging.Logger

class Runner(private val parser: Parser, private val graphBuilder: GraphBuilder,
            private val taskExecutor: TaskExecutor, private val logger: Logger) {

    private val usedBuildFilesSet: TreeSet<String> = TreeSet()
    private val filesQueue: Queue<String> = LinkedList()

    private var tasks = mapOf<String, Task>()
    private var targetTask: String? = null

    fun parseTarget(fullPathTask: String) {
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
}