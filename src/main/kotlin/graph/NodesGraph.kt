package graph

import task.Task
import java.util.LinkedList
import java.util.Queue
import kotlin.math.max

class NodesGraph : Graph {
    private val nodes: MutableMap<String, Node>
    private val initialNode = Node()

    init {
        nodes = mutableMapOf("initialNode" to initialNode)
    }

    override fun insertTaskWithDependencies(task: Task, tasksMap: Map<String, Task>) {
        val node: Node
        if (nodes.containsKey(task.name)) {
            node = nodes[task.name]!!
        } else {
            node = Node(task, level = 1)
            initialNode.children.add(node)
        }

        for (dependency in task.deps) {
            val depName = dependency.taskName
            if (!tasksMap.containsKey(depName)) {
                throw IllegalArgumentException("Task with name $depName not found")
            }
            val depNode: Node
            if (!nodes.containsKey(depName)) {
                depNode = Node(tasksMap[depName])
                nodes[depName] = depNode
                node.children.add(depNode)
            } else {
                depNode = nodes[depName]!!
                node.children.add(depNode)
            }
            if (depNode.parents.size == 1 && depNode.parents[0] === initialNode) {
                depNode.parents.removeAt(0)
            }
            depNode.parents.add(depNode)
            depNode.level = max(depNode.level, node.level + 1)
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

    private class Node(
        val task: Task? = null,
        var level: Int = 0,
        val children: MutableList<Node> = mutableListOf(),
        val parents: MutableList<Node> = mutableListOf()
    )
}
