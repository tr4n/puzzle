package com.tr4n.puzzle.ui.dashboard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.model.Preview
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.type.Category
import com.tr4n.puzzle.util.FileUtils
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.lang.Integer.max

class DashboardViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    val recentPreviews = MutableLiveData<List<Preview>>()
    val myPreviews = MutableLiveData<List<Preview>>()
    val categoryPreviews = MutableLiveData<List<Preview>>()

    val isTemporaryFileSaved = MutableLiveData<Boolean>()

    fun getLocalChallenges() = viewModelScope.launch {
        val challenges = challengeRepository.getChallenges()
        getRecentPreviewData(challenges)
        getMyPreviewData(challenges)
        getCategoryPreviewData(challenges)
    }

    private fun getMyPreviewData(challenges: List<Challenge>) {
        val displayItemCount = 5
        val myChallenges = challenges.filter { it.type == 0 }
        val moreItemCount = max(0, myChallenges.size - displayItemCount)
        val previews = myChallenges
            .sortedByDescending { it.createdAt }
            .take(displayItemCount)
            .mapIndexed { index, challenge ->
                Preview(
                    challenge = challenge,
                    moreCount = if (index == displayItemCount - 1) moreItemCount else 0
                )
            }
        myPreviews.value = listOf(Preview()) + previews
    }

    private fun getRecentPreviewData(challenges: List<Challenge>) {
        val previews = challenges.sortedByDescending { it.updatedAt }
            .take(6)
            .map { challenge ->
                Preview(challenge)
            }
        recentPreviews.value = previews
    }

    private fun getCategoryPreviewData(challenges: List<Challenge>) {
        val allPreviews = challenges.groupBy { it.type }
            .mapNotNull { (type, challengeList) ->
                val category = Category.fromValue(type)
                val challenge = challengeList.firstOrNull()
                if (category != null && challenge != null) {
                    Preview(challenge, category.keyword)
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
