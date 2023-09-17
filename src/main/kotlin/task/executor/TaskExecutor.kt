package task.executor

import task.Task

interface TaskExecutor {

    fun executeTasks(tasksQueue: List<Task>)

}
