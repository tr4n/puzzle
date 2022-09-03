package com.tr4n.puzzle.util.solution

import android.util.Log
import com.tr4n.puzzle.data.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class PuzzleGraph(private val initState: State) {

    private val openSet = NodeSet()

    private val closeSet = NodeSet()

    suspend fun search(): List<State>? = withContext(Dispatchers.IO) {
        val startNode = Node(initState)
        openSet += startNode
        while (openSet.isNotEmpty()) {
            val node = openSet.poll()
            if (node.isTarget) {
                return@withContext trackQueue(node)
            }
            Log.d("TAG", "search: ${openSet.size}")
            closedSet += node
            val nextNodes = node.getNextNodes()
            for (nextNode in nextNodes) {
                when {
                  //  nextNode !in openSet &&
                        nextNode !in closedSet -> {
                        openSet += nextNode
                    }
                    nextNode in openSet -> {
                        val openNodeCost = openSet[nextNode.code]?.costFromStart ?: continue
                        if (openNodeCost > nextNode.costFromStart) {
                            nextNode.parentCode = node.code
                            openSet[nextNode.code] = nextNode
                        }
                    }

                    nextNode in closeSet -> {
                        val closedNodeCost = closeSet[nextNode.code]?.costFromStart ?: continue
                        if (closedNodeCost > nextNode.costFromStart) {
                            nextNode.parentCode = node.code
                            closeSet[nextNode.code] = nextNode
                            closeSet.updateChildrenOf(nextNode)
                            openSet.updateChildrenOf(nextNode)
                        }
                    }
                }
            }
        }
        return@withContext null
    }

    private fun track(node: Node): List<State> {
        val states = mutableListOf(node.state)
        var currentNode = node
        while (currentNode.parentCode != null) {
            currentNode = closeSet[currentNode.parentCode] ?: break
            states.add(currentNode.state)
        }
        return states.reversed()
    }

    private val closedSet = NodeQueueSet()
    private val heap = PriorityQueue<Node>()

    private fun trackQueue(node: Node): List<State> {
        val states = mutableListOf(node.state)
        var currentNode = node
        while (currentNode.parentCode != null) {
            currentNode = closedSet[currentNode.parentCode] ?: break
            states.add(currentNode.state)
        }
        return states.reversed()
    }

    class NodeSet {

        private var nodes = mutableMapOf<String, Node>()
        private val nodeCodeSet = mutableSetOf<String>()

        val size get() = nodes.size

        operator fun plusAssign(node: Node) {
            nodes[node.code] = node
            nodeCodeSet.add(node.code)
//            priorityQueue.add(node.cost to node)
            Log.d("TAG", "plusAssign: ${nodeCodeSet.size}")
        }

        operator fun contains(node: Node): Boolean {
            return node.code in nodeCodeSet
        }

        operator fun set(code: String, node: Node) {
            nodes[code] = node
        }

        operator fun get(code: String?): Node? {
            return nodes[code]
        }

        fun poll(): Node {
            val node = nodes.map { it.value }.minBy { it.cost }
            nodes.remove(node.code)
            return node
        }

        fun isNotEmpty() = nodes.isNotEmpty()

        fun updateChildrenOf(parentNode: Node) {
            parentNode.childrenNodeCodes.forEach {
                val node = nodes[it] ?: return@forEach
                nodes[it]?.costFromStart = parentNode.calculateToNextNode(node.state)
            }
        }
    }

    class NodeQueueSet {

        private var nodes = mutableMapOf<String, Node>()
        private val nodeCodeSet = mutableSetOf<String>()

        operator fun plusAssign(node: Node) {
            nodes[node.code] = node
            nodeCodeSet.add(node.code)
        }

        operator fun minusAssign(node: Node) {
            nodes.remove(node.code)
            nodeCodeSet.remove(node.code)
        }

        operator fun contains(node: Node): Boolean {
            return node.code in nodeCodeSet
        }

        operator fun set(code: String, node: Node) {
            nodes[code] = node
        }

        operator fun get(code: String?): Node? {
            return nodes[code]
        }

        fun pop(): Node {
            val node = nodes.map { it.value }.minBy { it.cost }
            nodes.remove(node.code)
            return node
        }
    }
}
