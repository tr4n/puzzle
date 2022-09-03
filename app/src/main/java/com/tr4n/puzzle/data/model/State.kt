package com.tr4n.puzzle.data.model

import com.tr4n.puzzle.data.type.Move
import com.tr4n.puzzle.extension.*
import kotlin.math.abs
import kotlin.math.sqrt

data class State(val puzzleIndexes: List<Int> = emptyList()) {

    private val size = sqrt(puzzleIndexes.size.toDouble()).toInt()

    val hashCode get() = puzzleIndexes.toStateCode()

    val isTarget get() = puzzleIndexes.indexAll { index, puzzleIndex -> puzzleIndex == index }

    val mahattanDistance
        get() = puzzleIndexes.sumIndexOf { index, puzzleIndex ->
            abs(puzzleIndex / size - index / size) + abs(puzzleIndex % size - index % size)
        }

    val wrongPositionCount
        get() = puzzleIndexes.indexCount { index, item ->
            index != item
        }

    fun move(move: Move): State {
        val currentEmptyIndex = puzzleIndexes.indexOfFirst { it == 0 }.takeIf { it > 0 } ?: 0
        val currentRow = currentEmptyIndex / size
        val currentColumn = currentEmptyIndex % size
        val delta = when {
            move == Move.LEFT && currentColumn < size - 1 -> 1
            move == Move.RIGHT && currentColumn > 0 -> -1
            move == Move.UP && currentRow < size - 1 -> size
            move == Move.DOWN && currentRow > 0 -> -size
            else -> return this
        }
        val nextIndex = currentEmptyIndex + delta

        val newPuzzles = puzzleIndexes.mapIndexed { index, puzzle ->
            when (index) {
                currentEmptyIndex -> puzzleIndexes[nextIndex]
                nextIndex -> puzzleIndexes[currentEmptyIndex]
                else -> puzzle
            }
        }
        return State(newPuzzles)
    }

    fun getNextStates(): List<State> {
        val currentEmptyIndex = puzzleIndexes.indexOfFirst { it == 0 }.takeIf { it > 0 } ?: 0
        val currentRow = currentEmptyIndex / size
        val currentColumn = currentEmptyIndex % size

        return Move.values().mapNotNull { move ->
            val delta = when {
                move == Move.LEFT && currentColumn < size - 1 -> 1
                move == Move.RIGHT && currentColumn > 0 -> -1
                move == Move.UP && currentRow < size - 1 -> size
                move == Move.DOWN && currentRow > 0 -> -size
                else -> return@mapNotNull null
            }
            val nextIndex = currentEmptyIndex + delta

            val newPuzzles = puzzleIndexes.toMutableList().apply {
                swap(currentEmptyIndex, nextIndex)
            }
            State(newPuzzles)
        }
    }
}
