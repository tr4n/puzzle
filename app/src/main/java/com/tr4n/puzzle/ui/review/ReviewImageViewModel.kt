package com.tr4n.puzzle.ui.review

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.BitmapUtils.cropCenter
import com.tr4n.puzzle.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class ReviewImageViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    val imageBitmap = MutableLiveData<Bitmap>()

    val challenge = MutableLiveData<Challenge>()

    fun getRecentTakenImageBitmap() {
        loading.value = true
        val bitmap = BitmapUtils.decodeSampledBitmapFromFile(
            FileUtils.temporaryImageFile,
            400,
            400
        )
        val rotate = BitmapUtils.getCameraPhotoOrientation(FileUtils.temporaryImageFile.path)
        val rotatedBitmap = BitmapUtils.rotateBitmap(bitmap, rotate)?.cropCenter()
        loading.value = false
        imageBitmap.value = rotatedBitmap ?: return
    }

    fun savePhoto() = viewModelScope.launch(Dispatchers.IO) {
        loading.postValue(true)
        val fileName = FileUtils.savePuzzlePhoto() ?: run {
            loading.postValue(false)
            return@launch
        }
        val newChallenge = Challenge(imageName = fileName)
        challengeRepository.addChallenge(newChallenge)
        loading.postValue(false)
        challenge.postValue(newChallenge)
        loading.postValue(false)
    }
}
