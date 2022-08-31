package com.tr4n.puzzle.util

import com.tr4n.puzzle.data.model.State


/**
 * Create a Direction Graph of blood pressures for find first valid bp set
 * Algorithm: Depth first search (DFS)
 * @see <img src="https://upload.wikimedia.org/wikipedia/commons/7/7f/Depth-First-Search.gif"/>
 *
 */
class PuzzleGraph(
    private val state: State
) {
    private val nodes = emptyList<Node>()

    private val graphEdges = Edges(nodes)

    private val selectedNodes = mutableListOf<Node>()

    private val openNodes = listOf<Node>()

    private val closedNodes = listOf<Node>()


    /**
     * search by dfs
     */
    private fun searchByDFS(): Any? {
        for (node in nodes) {
            selectedNodes.clear()
            nodes.forEach {
                it.isVisited = false
            }
            if (visit(node)) {
                return selectedNodes.map { it.state }
            }
        }
        return null
    }

    /**
     * Visit node
     * Check if [selectedNodes] is a valid set then finish
     * otherwise, visit node's children
     * @param node: [Node]
     */
    private fun visit(node: Node): Boolean {
        node.isVisited = true
        selectedNodes.add(node)

        if (node.isTarget) {
            return true
        }

        for (edge in graphEdges[node]) {
            val nextNode = edge.end
            val shouldVisitNextNode = !nextNode.isVisited

            if (shouldVisitNextNode) {
                if (visit(nextNode)) return true

                // clear node's status when turn back
                nextNode.isVisited = false
                selectedNodes.removeLastOrNull()
            }
        }
        return false
    }

    /**
     * Each BP is a [Node] has attributes:
     * @param index: index of [State] in [state], the index of node in [nodes] too.
     * @param state: [State]
     * @param isVisited:  [Boolean] true if dfs visited
     */
    data class Node(
        val index: Int,
        val state: State,
        var isVisited: Boolean = false
    ) {

        val isTarget get() = state.isTarget()

        companion object {
            /**
             * create new node from [State] and index
             * @param index: [Int] index of bp
             * @param state: [State]
             */
            fun initNode(index: Int, state: State): Node {
                return Node(index = index, state = state)
            }
        }
    }

    /**
     * Two node is a edge if time between 2 bp
     *  + is not out of range restPeriodBetweenMeasurements to max (for primary measurement)
     *  + is not out of range restTimeConfirmationMeasurement to max extra (for extra measurement)
     */
    data class Edge(
        val start: Node,
        val end: Node,
    ) {
        companion object {
            /**
             * The edge is created if  2 node is valid between time
             */
            fun createEdgeOrNull(start: Node, end: Node): Edge? {
                return Edge(start, end)
            }
        }
    }

    /**
     * All edges of graph
     */
    class Edges(nodes: List<Node>) {

        private val edgeList = nodes.mapIndexed { index, startNode ->
            nodes.subList(index, nodes.size).mapNotNull { endNode ->
                Edge.createEdgeOrNull(startNode, endNode)
            }
        }

        operator fun get(start: Node): List<Edge> {
            return edgeList.getOrNull(start.index) ?: emptyList()
        }

        operator fun get(start: Node, end: Node): Edge? {
            return edgeList[start.index].find { it.end.index == end.index }
        }
    }
}
