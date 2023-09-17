package task.executor

import task.Task
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskExecutorImplTest {
    private val tasks = listOf(
        Task("task_1", Task.Companion.TaskType.BINARY, "echo 'task_1' < {srcs}", emptyList(),
            listOf(), ""),
        Task("task_2", Task.Companion.TaskType.SCRIPT, "echo 'task_2' < {deps}", listOf("Class.kt"),
            listOf(Task.Dependency("task_1", "")), "")
    )

    @Test
    fun testExecuteTasks() {

    }
}