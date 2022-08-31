package com.tr4n.puzzle.data.model

import com.tr4n.puzzle.data.type.Move
import kotlin.math.sqrt

data class State(val puzzles: List<Puzzle>) {

    private val size = sqrt(puzzles.size.toDouble()).toInt()

    fun isTarget(): Boolean {
        puzzles.forEachIndexed { index, puzzle -> if (puzzle.index != index) return false }
        return true
    }

    fun move(move: Move): State {
        val currentEmptyIndex = puzzles.indexOfFirst { it.index == 0 }.takeIf { it > 0 } ?: 0
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

        val newPuzzles = puzzles.mapIndexed { index, puzzle ->
            when (index) {
                currentEmptyIndex -> puzzles[nextIndex]
                nextIndex -> puzzles[currentEmptyIndex]
                else -> puzzle
            }
        }
        return State(newPuzzles)
    }
}
