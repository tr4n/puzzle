package com.tr4n.puzzle.ui.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.io.File

class ReviewImageViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    val imageFile = MutableLiveData<File>()

    val challenge = MutableLiveData<Challenge>()

    fun getRecentTakenImageBitmap() {
        imageFile.value = FileUtils.temporaryImageFile
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
