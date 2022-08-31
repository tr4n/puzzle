package com.tr4n.puzzle.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Puzzle
import com.tr4n.puzzle.data.model.State
import com.tr4n.puzzle.data.type.Move
import com.tr4n.puzzle.di.App
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.BitmapUtils.cropCenter
import com.tr4n.puzzle.util.BitmapUtils.split
import com.tr4n.puzzle.util.PuzzleGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.lang.Integer.min

class GameViewModel : BaseViewModel() {

    val currentSize = MutableLiveData<Int>()

    val currentState = MutableLiveData<State>()

    private var originPuzzles = listOf<Puzzle>()

    private var currentPuzzles = listOf<Puzzle>()

    private val fullBitmap by lazy {
        BitmapUtils.decodeSampledBitmapFromResource(
            App.context.resources,
            R.drawable.sample,
            400,
            400
        ).cropCenter()
    }

    fun updateSize(isIncrease: Boolean) {
        val size = currentSize.value?.let {
            max(3, it + if (isIncrease) 1 else -1)
        } ?: 3
        currentSize.value = min(5, size)
        val smallBitmaps = fullBitmap.split(size)
        originPuzzles = smallBitmaps.mapIndexed { index, bitmap ->
            Puzzle(index, bitmap = bitmap)
        }
        currentPuzzles = originPuzzles
        currentState.value = State(currentPuzzles)
    }

    fun swipePuzzle(move: Move) {
        currentState.value = currentState.value?.move(move)
    }

    fun shuffle() {
        var state = currentState.value ?: return
        var previousMove = Move.DOWN
        repeat(100) {
            val move = Move.values().filter { (it.value + 2) % 4 != previousMove.value }.random()
            state = state.move(move)
            previousMove = move
        }
        currentState.value = state
    }

    fun solve() {
        val stateCurrent = currentState.value ?: return
        viewModelScope.launch {
            loading.value = true
            val solvedStates = PuzzleGraph(stateCurrent).search() ?: emptyList()
            loading.value = false
            for (state in solvedStates) {
                delay(500L)
                currentState.value = state
            }
            messageSnackBar.value = if (solvedStates.isEmpty()) "None" else "Done"
        }
    }
}

