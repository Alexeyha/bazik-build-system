package graph

import task.Task
import java.io.File
import java.util.LinkedList
import kotlin.math.max
import kotlin.text.StringBuilder

class GraphNodes : Graph {
    private val nodes: MutableMap<String, Node>
    private val initialNode = Node()

    init {
        nodes = mutableMapOf("initialNode" to initialNode)
    }

    override fun insertTaskWithDependencies(task: Task, tasksMap: Map<String, Task>) {
        val node: Node
        if (nodes.containsKey(task.name)) {
            node = nodes[task.name]!!
            if (task.deps.isNotEmpty()) {
                node.parents.clear()
            }
        } else {
            node = Node(task, level = 1)
            nodes[task.name] = node
            if (task.deps.isEmpty()) {
                initialNode.children.add(node)
            }
        }

        for (dependency in task.deps) {
            val depName = dependency.taskName
            if (!tasksMap.containsKey(depName)) {
                throw IllegalArgumentException("Task with name $depName not found")
            }
            val depNode: Node
            if (!nodes.containsKey(depName)) {
                depNode = Node(tasksMap[depName], level = 1)
                nodes[depName] = depNode
                initialNode.children.add(depNode)
            } else {
                depNode = nodes[depName]!!
                depNode.children.add(node)
            }
            node.parents.add(depNode)
            depNode.children.add(node)
        }
    }

    override fun syncAllDependencies() {
        val nodesQueue = LinkedList<Node>()
        val nodesToDepsCount = HashMap<Node, Int>()
        for (childNode in initialNode.children) {
            nodesQueue.add(childNode)
        }
        while (nodesQueue.isNotEmpty()) {
            val node = nodesQueue.remove()
            for (childNode in node.children) {
                childNode.level = max(childNode.level, node.level + 1)
                if (nodesToDepsCount.containsKey(childNode)) {
                    nodesToDepsCount[childNode] = nodesToDepsCount[childNode]!!.plus(1)
                } else {
                    nodesToDepsCount[childNode] = 1
                }
                if (nodesToDepsCount[childNode] == childNode.task!!.deps.size) {
                    nodesQueue.add(childNode)
                }
            }
        }
    }

    override fun buildQueueOfTasks(target: Task) : List<Task> {
        val nodesList = mutableListOf<Node>()
        val nodesQueue = LinkedList<Node>()
        val usedNodes = HashMap<Node, Boolean>()

        usedNodes[initialNode] = true
        nodesQueue.add(nodes[target.name]!!)
        while (nodesQueue.isNotEmpty()) {
            val node = nodesQueue.remove()
            nodesList.add(node)
            for (nodeParent in node.parents) {
                if (usedNodes.containsKey(nodeParent)) continue
                usedNodes[nodeParent] = true
                nodesQueue.add(nodeParent)
            }
        }
        nodesList.sortBy { node -> node.level }
        return nodesList.map { node -> node.task!! }
    }

    override fun renderEntireGraphInFile(filePath: String) {
        val file = File(filePath)
        if (!file.exists() || !file.isFile) {
            throw IllegalArgumentException("File on path $filePath to write graph not found")
        }
        val renderedGraph = StringBuilder()
        val nodesList = nodes.values.toMutableList()
        nodesList.sortBy { node -> node.level }
        val nodesToId = HashMap<Node, Int>()
        var currentLevel = 1
        for ((cnt, node) in nodesList.withIndex()) {
            if (node === initialNode) continue
            if (node.level > currentLevel) renderedGraph.append("\n")
            renderedGraph.append("${node.task?.name}: id $cnt, deps: [")
            for (nodePar in node.parents) {
                if (nodePar === node.parents.last()) renderedGraph.append(nodesToId[nodePar])
                else renderedGraph.append("${nodesToId[nodePar]}, ")
            }
            renderedGraph.append("]   |    ")
            nodesToId[node] = cnt
            currentLevel = node.level
        }
        file.writeText(renderedGraph.toString())
    }


    private class Node(
        val task: Task? = null,
        var level: Int = 0,
        val children: MutableList<Node> = mutableListOf(),
        val parents: MutableList<Node> = mutableListOf()
    )
}
