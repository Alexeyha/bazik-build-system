package graph.builder

import graph.Graph
import task.Task

class GraphBuilderImpl : GraphBuilder {
    override fun buildGraph(graph: Graph, tasks: Map<String, Task>) {
        for ((_, task) in tasks) {
            graph.insertTaskWithDependencies(task, tasks)
        }
        graph.syncAllDependencies()
    }

}