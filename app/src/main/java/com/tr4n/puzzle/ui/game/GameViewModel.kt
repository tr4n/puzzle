package com.tr4n.puzzle.ui.game

import androidx.lifecycle.MutableLiveData
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Puzzle
import com.tr4n.puzzle.data.model.State
import com.tr4n.puzzle.data.type.Move
import com.tr4n.puzzle.di.App
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.BitmapUtils.cropCenter
import com.tr4n.puzzle.util.BitmapUtils.split
import java.lang.Integer.max
import java.lang.Integer.min

class GameViewModel : BaseViewModel() {

    val currentSize = MutableLiveData<Int>()

    val puzzles = MutableLiveData<List<Puzzle>>()

    val currentState = MutableLiveData<State>()

    private var originPuzzles = listOf<Puzzle>()

    private var currentPuzzles = listOf<Puzzle>()

    private val fullBitmap by lazy {
        BitmapUtils.decodeSampledBitmapFromResource(
            App.context.resources,
            R.drawable.img_sample,
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
        if (currentState.value?.isTarget() == true) {
            messageSnackBar.value = "target"
        }
    }
}

