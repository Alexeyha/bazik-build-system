package graph.builder

import graph.Graph
import task.Task

interface GraphBuilder {
    fun buildGraph(graph: Graph, tasks: Map<String, Task>)
}