package graph.builder

import graph.Graph
import logger.Logger
import task.Task

class GraphBuilderImpl : GraphBuilder {
    override fun buildGraph(graph: Graph, tasks: Map<String, Task>) {
        Logger.get().debug("GraphBuilder starts buildGraph")
        for ((_, task) in tasks) {
            graph.insertTaskWithDependencies(task, tasks)
        }
        graph.syncAllDependencies()
    }
}
