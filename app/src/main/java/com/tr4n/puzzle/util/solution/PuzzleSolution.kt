package com.tr4n.puzzle.util.solution

import com.tr4n.puzzle.data.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class PuzzleSolution(private val initState: State) {
    private val closedSet = NodeQueueSet()
    private val heap = PriorityQueue<Node>()
    private val openSet = mutableSetOf<String>()

    suspend fun search(): List<State>? = withContext(Dispatchers.IO) {
        val startNode = Node(initState)
        heap.add(startNode)
        closedSet += startNode
        while (heap.isNotEmpty()) {
            val node = heap.poll() ?: break
            openSet.remove(node.code)

            if (node.isTarget) {
                return@withContext trackQueue(node)
            }
            val nextNodes = node.getNextNodes()

            for (nextNode in nextNodes) {
                if (nextNode !in closedSet) {
                    heap.add(nextNode)
                    openSet.add(nextNode.code)
                    closedSet += nextNode
                }
            }
        }
        return@withContext null
    }

    private fun trackQueue(node: Node): List<State> {
        val states = mutableListOf(node.state)
        var currentNode = node
        while (currentNode.parentCode != null) {
            currentNode = closedSet[currentNode.parentCode] ?: break
            states.add(0, currentNode.state)
        }
        return states
    }


    class NodeQueueSet {

        private var nodes = mutableMapOf<String, Node>()

        operator fun plusAssign(node: Node) {
            nodes[node.code] = node
        }

        operator fun minusAssign(node: Node) {
            nodes.remove(node.code)
        }

        operator fun contains(node: Node): Boolean {
            return nodes[node.code] != null
        }

        operator fun set(code: String, node: Node) {
            nodes[code] = node
        }

        operator fun get(code: String?): Node? {
            return nodes[code]
        }
    }
}
