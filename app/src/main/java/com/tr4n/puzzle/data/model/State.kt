package com.tr4n.puzzle.data.model

import com.tr4n.puzzle.data.type.Move
import kotlin.math.sqrt

data class State(val puzzles: List<Puzzle>) {

    private val size = sqrt(puzzles.size.toDouble()).toInt()

    fun isTarget(): Boolean {
        puzzles.forEachIndexed { index, puzzle -> if (puzzle.id != index) return false }
        return true
    }

    fun move(move: Move): State {
        val currentEmptyIndex = puzzles.indexOfFirst { it.id == 0 }.takeIf { it > 0 } ?: 0
        val delta = when (move) {
            Move.LEFT -> 1
            Move.RIGHT -> -1
            Move.UP -> size
            Move.DOWN -> -size

        }
        val nextIndex = currentEmptyIndex + delta
        if (nextIndex !in puzzles.indices) return this

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
