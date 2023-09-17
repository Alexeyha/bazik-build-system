package graph

import task.Task

interface Graph {

    fun insertTaskWithDependencies(task: Task, tasksMap: Map<String, Task>)

    fun syncAllDependencies()

    fun buildQueueOfTasks(target: Task) : List<Task>

    fun renderEntireGraphInFile(filePath: String)
}