package com.tr4n.puzzle.util.solution

import com.tr4n.puzzle.data.model.State

data class Node(
    val state: State,
    var costFromStart: Int = 0,
    var parentCode: String? = null,
) : Comparable<Node> {

    private val costToTarget = state.mahattanDistance * 10 + state.wrongPositionCount

    var childrenNodeCodes = emptyList<String>()

    val code = state.hashCode

    val isTarget = state.isTarget

    val cost get() = costFromStart + costToTarget * 10

    fun calculateToNextNode(state: State): Int {
        return when {
            state.hashCode == parentCode -> 10000
            state.mahattanDistance <= this.state.mahattanDistance -> costFromStart
            state.wrongPositionCount <= this.state.wrongPositionCount -> costFromStart + 10
            else -> costFromStart + 100
        }
    }

    fun getNextNodes(): List<Node> {
        val nodes = state.getNextStates().mapNotNull { state ->
            Node(state, calculateToNextNode(state), code).takeIf {
                it.code != parentCode
            }
        }
        childrenNodeCodes = nodes.map { it.code }
        return nodes
    }

    override fun compareTo(other: Node): Int {
        return when {
            cost > other.cost -> 1
            cost == other.cost -> 0
            else -> -1
        }
    }
}
