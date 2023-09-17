package graph

import task.Task

interface Graph {

    fun insertTaskWithDependencies(task: Task, tasksMap: Map<String, Task>)

    fun buildQueueOfTasks(target: Task) : List<Task>
}