package com.tr4n.puzzle.ui.game

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.Puzzle
import com.tr4n.puzzle.di.App
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.BitmapUtils.cropCenter
import com.tr4n.puzzle.util.BitmapUtils.split

class GameViewModel : BaseViewModel() {

    val currentSize = MutableLiveData<Int>()

    val puzzles = MutableLiveData<List<Puzzle>>()

    val bitmap = MutableLiveData<Bitmap>()

    val fullBitmap by lazy {
        BitmapUtils.decodeSampledBitmapFromResource(
            App.context.resources,
            R.drawable.sample,
            400,
            400
        ).cropCenter()
    }

    fun increaseSize() {
        val size = (currentSize.value ?: 0) + 1
        currentSize.value = size
        val smallBitmaps = fullBitmap.split(size)
        puzzles.value = smallBitmaps.mapIndexed { index, bitmap ->
            Puzzle(index, bitmap = bitmap)
        }
    }
}

