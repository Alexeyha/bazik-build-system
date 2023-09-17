package task.executor

import task.Task

interface TaskExecutor {

    /**
     * Execute all tasks in the queue of the tasks
     *
     * @param tasksQueue queue of the tasks to execute
     */
    fun executeTasks(tasksQueue: List<Task>)

}
