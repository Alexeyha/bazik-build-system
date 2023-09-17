package task.executor

import task.Task
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.io.path.createTempFile


class TaskExecutorImpl : TaskExecutor {
    val executedTasksToOutput = mutableMapOf<Task, String>()

    override fun executeTasks(tasksQueue: List<Task>) {
//        for (task in tasksQueue) {
//            execute(task)
//        }
        val commands = listOf(
            "mkdir Dir1",
            "cd Dir1",
            "echo 'Hello' > hello.txt",
            "ls -l"
        )

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
