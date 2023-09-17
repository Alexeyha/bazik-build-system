package graph

import graph.builder.GraphBuilderImpl
import task.Task
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.io.path.createTempFile

class GraphNodesTest {
    private val graphBuilder = GraphBuilderImpl()
    private val graphNodes = GraphNodes()

    private val tasks = mapOf(
        "compile" to Task("compile", Task.Companion.TaskType.BINARY, "", emptyList(), listOf(), ""),
        "compile_1" to Task("compile_1", Task.Companion.TaskType.BINARY, "", emptyList(),
            listOf(Task.Dependency("compile", ""), Task.Dependency("run", "")), ""),
        "run" to Task("run", Task.Companion.TaskType.BINARY, "", emptyList(),
            listOf(Task.Dependency("compile", "")), ""),
        "run_1" to Task("run_1", Task.Companion.TaskType.BINARY, "", emptyList(),
            listOf(Task.Dependency("compile_1", "")), "")
    )

    @Test
    fun testBuildGraph() {
        val expectedRenderedGraph = """
            compile: id 1, deps: []   |    
            run: id 2, deps: [1]   |    
            compile_1: id 3, deps: [1, 2]   |    
            run_1: id 4, deps: [3]   |    
        """.trimIndent()

        graphBuilder.buildGraph(graphNodes, tasks)
        val file = createTempFile("test_graph", ".txt").toFile()
        try {
            graphNodes.renderEntireGraphInFile(file.absolutePath)
            val outputRenderedGraph = file.readText()
            assertEquals(expectedRenderedGraph, outputRenderedGraph)
        } finally {
            file.delete()
        }
    }
}
