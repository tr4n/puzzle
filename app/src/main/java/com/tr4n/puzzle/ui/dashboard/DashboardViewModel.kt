package com.tr4n.puzzle.ui.dashboard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.PreviewChallenge
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.BitmapUtils.PREVIEW_DECODED_IMAGE_SIZE
import com.tr4n.puzzle.util.BitmapUtils.cropCenter
import com.tr4n.puzzle.util.FileUtils
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class DashboardViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    private var allPreviews = emptyList<PreviewChallenge>()

    val recentPreviews = MutableLiveData<List<PreviewChallenge>>()
    val myPreviews = MutableLiveData<List<PreviewChallenge>>()
    val categoryPreviews = MutableLiveData<List<PreviewChallenge>>()

    val isTemporaryFileSaved = MutableLiveData<Boolean>()

    fun getLocalChallenges() = viewModelScope.launch {
        val challenges = challengeRepository.getChallenges()
        allPreviews = challenges.map { challenge ->
            val file = FileUtils.getImageFile(challenge.imageName)
            val bitmap = BitmapUtils.decodeSampledBitmapFromFile(
                file,
                PREVIEW_DECODED_IMAGE_SIZE,
                PREVIEW_DECODED_IMAGE_SIZE
            ).cropCenter()
            PreviewChallenge(challenge, bitmap)
        }
        getRecentPreviewData()
        getMyPreviewData()
        getCategoryPreviewData()
    }

    private fun getMyPreviewData() {
        val previews = allPreviews.sortedByDescending { it.challenge.createdAt }.take(5)
        myPreviews.value = listOf(PreviewChallenge()) + previews
    }

    private fun getRecentPreviewData() {
        val previews = allPreviews.sortedByDescending { it.challenge.updatedAt }.take(3)
        recentPreviews.value = previews
    }

    private fun getCategoryPreviewData() {
        categoryPreviews.value = allPreviews
    }

    fun saveSelectedImage(uri: Uri?) = viewModelScope.launch {
        loading.value = true
        FileUtils.saveTemporaryFileFromUri(uri)
        isTemporaryFileSaved.value = true
        loading.value = false
    }
}
