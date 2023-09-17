package graph

import task.Task

interface Graph {

    /**
     * Insert task into dependency graph and bind it with its dependencies.
     *
     * @param task task to insert
     * @param tasksMap map with all tasks
     */
    fun insertTaskWithDependencies(task: Task, tasksMap: Map<String, Task>)

    /**
     * Synchronise all invariants in the graph
     */
    fun syncAllDependencies()

    /**
     * Build queue of tasks in the correct order to execute.
     *
     * @param target target task
     * @return list of task to execute where last node is target one
     */
    fun buildQueueOfTasks(target: Task) : List<Task>

    /**
     * Render the graph in the concrete file
     *
     * @param filePath path to the file in which graph will be rendered.
     */
    fun renderEntireGraphInFile(filePath: String)
}