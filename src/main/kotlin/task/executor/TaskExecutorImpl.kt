package task.executor

import task.Task
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.io.path.createTempFile

class TaskExecutorImpl : TaskExecutor {
    private val executedTasksToOutput = mutableMapOf<String, String>()

    private fun getTaskAction(task: Task) : String {
        val srcs = task.srcs.joinToString(separator = " ")
        val outputs = task.deps.map {
            taskDep -> executedTasksToOutput[taskDep.taskName]
        }.joinToString(" ")
        return task.action.replace("{srcs}", srcs).replace("{deps}", outputs)
    }

    override fun executeTasks(tasksQueue: List<Task>) {
        val commands = mutableListOf<String>()
        for (task in tasksQueue) {
            commands.add(getTaskAction(task))
        }

        val scriptContent = commands.joinToString(separator = "\n")

        val scriptFile = createTempFile("script", ".sh").toFile()
        scriptFile.writeText(scriptContent)

        try {
            scriptFile.setExecutable(true)

            val process = Runtime.getRuntime().exec(scriptFile.absolutePath)

            val inputStream = process.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                println(line)
            }

            val exitCode = process.waitFor()
            println("Process exited with code $exitCode")

        } finally {
            scriptFile.delete()
        }
    }
}
