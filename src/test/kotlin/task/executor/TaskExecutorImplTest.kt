package task.executor

import task.Task
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.io.path.createTempFile

class TaskExecutorImplTest {
    private val taskExecutor = TaskExecutorImpl()

    @Test
    fun testExecuteTasks() {
        val file = createTempFile("test_task_executor", ".txt").toFile()
        try {
            val tasks = listOf(
                Task("task_1", Task.Companion.TaskType.BINARY, "echo 'task_1' >> {srcs}",
                    listOf("${file.absoluteFile}"), emptyList(), "${file.absoluteFile}"),
                Task("task_2", Task.Companion.TaskType.SCRIPT, "echo 'task_2' >> {deps}", listOf("Class.kt"),
                    listOf(Task.Dependency("task_1", "")), "")
            )
            taskExecutor.executeTasks(tasks)
            val outputExec = file.readText()
            val expectedExec = "task_1\ntask_2\n"
            assertEquals(expectedExec, outputExec)
        } finally {
            file.delete()
        }
    }
}