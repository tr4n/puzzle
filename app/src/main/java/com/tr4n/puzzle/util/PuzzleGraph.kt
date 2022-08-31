package com.tr4n.puzzle.util

import com.tr4n.puzzle.data.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class PuzzleGraph(initState: State) {

    private val openSet = NodeSet(listOf(Node(initState)))

    private val closeSet = NodeSet()

    private val openQueue = NodeQueue(listOf(Node(initState)))

    suspend fun search(): List<State>? = withContext(Dispatchers.IO) {

        while (openSet.isNotEmpty()) {
            val node = openSet.pop() ?: break
            if (node.isTarget) {
                return@withContext track(node)
            }

            val nextNodes = node.getNextNodes()
            for (nextNode in nextNodes) {
                when {
                    nextNode !in openSet && nextNode !in closeSet -> openSet += nextNode
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
            closeSet += node
        }
        return@withContext null
    }

    private val closedSet = mutableSetOf<String>()
    suspend fun searchByQueue(): List<State>? = withContext(Dispatchers.IO) {

        while (openQueue.isNotEmpty()) {
            val node = openQueue.pop() ?: break
            if (node.isTarget) {
                return@withContext track(node)
            }
            closedSet.add(node.code)

            val nextNodes = node.getNextNodes()
            openQueue += nextNodes.filterNot { it.code in closedSet }
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

    data class Node(
        val state: State,
        var costFromStart: Int = 0,
        var parentCode: String? = null
    ) {

        private val costToTarget = state.mahattanDistance

        val code = state.puzzles.joinToString(prefix = "", separator = ",", postfix = "") {
            it.index.toString()
        }

        val isTarget = state.isTarget

        val cost get() = costFromStart  + costToTarget

        val costToNextNode get() = costFromStart + 1

        fun getNextNodes(): List<Node> {
            return state.getNextStates().map { state ->
                Node(state, costToNextNode, code)
            }
        }
    }

    class NodeSet(initNodes: List<Node> = emptyList()) {

        private var nodes = initNodes.toMutableList()
        private val nodeCodeSet = initNodes.map { it.code }.toMutableSet()

        operator fun plusAssign(node: Node) {
            nodes.add(node)
            nodes.sortBy { it.cost }
            nodeCodeSet.add(node.code)
        }

        operator fun contains(node: Node): Boolean {
            return node.code in nodeCodeSet
        }

        operator fun set(code: String, node: Node): Boolean {
            val index =
                nodes.indexOfFirst { it.code == code }.takeIf { it >= 0 } ?: return false
            nodes[index] = node
            return true
        }

        operator fun get(code: String?): Node? {
            val index =
                nodes.indexOfFirst { it.code == code }.takeIf { it >= 0 } ?: return null
            return nodes[index]
        }

        fun pop(): Node? {
            val node = nodes.minBy { it.cost }
            return node.takeIf { nodes.remove(node) }
        }

        fun isNotEmpty() = nodes.isNotEmpty()

        fun updateChildrenOf(parentNode: Node) {
            nodes = nodes.map {
                if (it.parentCode != parentNode.code) return@map it
                it.copy(costFromStart = parentNode.costFromStart + 1)
            }.toMutableList()
        }
    }

    class NodeQueue(initNodes: List<Node> = emptyList()) {

        private var nodes = PriorityQueue<Node> { first, second ->
            first.cost - second.cost
        }.apply {
            addAll(initNodes)
        }
        private val nodeCodeSet = initNodes.map { it.code }.toMutableSet()

        operator fun plusAssign(nodes: List<Node>) {
            this.nodes.addAll(nodes)
            nodeCodeSet.addAll(nodes.map { it.code })
        }

        operator fun contains(node: Node): Boolean {
            return node.code in nodeCodeSet
        }

        fun pop(): Node? {
            return nodes.poll()
        }

        fun isNotEmpty() = nodes.isNotEmpty()
    }
}
