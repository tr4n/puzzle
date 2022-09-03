package com.tr4n.puzzle.ui.dashboard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.model.PreviewChallenge
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.type.Category
import com.tr4n.puzzle.util.FileUtils
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class DashboardViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    val recentPreviews = MutableLiveData<List<PreviewChallenge>>()
    val myPreviews = MutableLiveData<List<PreviewChallenge>>()
    val categoryPreviews = MutableLiveData<List<PreviewChallenge>>()

    val isTemporaryFileSaved = MutableLiveData<Boolean>()

    fun getLocalChallenges() = viewModelScope.launch {
        val challenges = challengeRepository.getChallenges()
        getRecentPreviewData(challenges)
        getMyPreviewData(challenges)
        getCategoryPreviewData(challenges)
    }

    private fun getMyPreviewData(challenges: List<Challenge>) {
        val previews = challenges.filter { it.type == 0 }
            .sortedByDescending { it.createdAt }
            .take(5)
            .map { challenge ->
                PreviewChallenge(challenge)
            }
        myPreviews.value = listOf(PreviewChallenge()) + previews
    }

    private fun getRecentPreviewData(challenges: List<Challenge>) {
        val previews = challenges.sortedByDescending { it.updatedAt }
            .take(3)
            .map { challenge ->
                PreviewChallenge(challenge)
            }
        recentPreviews.value = previews
    }

    private fun getCategoryPreviewData(challenges: List<Challenge>) {
        val allPreviews = challenges.groupBy { it.type }
            .mapNotNull { (type, challengeList) ->
                val category = Category.fromValue(type)
                val challenge = challengeList.firstOrNull()
                if (category != null && challenge != null) {
                    PreviewChallenge(challenge, category.keyword)
                } else null
            }
        categoryPreviews.value = allPreviews
    }

    fun saveSelectedImage(uri: Uri?) = viewModelScope.launch {
        loading.value = true
        FileUtils.saveTemporaryFileFromUri(uri)
        isTemporaryFileSaved.value = true
        loading.value = false
    }
}
