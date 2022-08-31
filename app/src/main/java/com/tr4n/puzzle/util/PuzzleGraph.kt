package com.tr4n.puzzle.util

import com.tr4n.puzzle.data.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PuzzleGraph(initState: State) {

    private val openSet = NodeSet(listOf(Node(initState)))

    private val closeSet = NodeSet()

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
                        val openNodeCost = openSet[nextNode.code]?.cost ?: continue
                        if (openNodeCost > nextNode.cost) {
                            nextNode.parentCode = node.code
                            openSet[nextNode.code] = nextNode
                        }
                    }

                    nextNode in closeSet -> {
                        val closedNodeCost = closeSet[nextNode.code]?.cost ?: continue
                        if (closedNodeCost > nextNode.cost) {
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
        var cost: Int = 0,
        var parentCode: String? = null,
        var isVisited: Boolean = false
    ) {

        val code = state.puzzles.joinToString(prefix = "", separator = ",", postfix = "") {
            it.index.toString()
        }

        val isTarget get() = state.isTarget

        fun estimateTo(node: Node): Int {
            return cost + node.state.mahattanDistance
        }

        fun getNextNodes(): List<Node> {
            return state.getNextStates().map { state ->
                Node(state, cost + state.mahattanDistance, code)
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
            return nodes.removeFirstOrNull()
        }

        fun isNotEmpty() = nodes.isNotEmpty()

        fun updateChildrenOf(parentNode: Node) {
            nodes = nodes.map {
                if (it.parentCode != parentNode.code) return@map it
                it.copy(cost = parentNode.estimateTo(it))
            }.toMutableList()
        }
    }
}
