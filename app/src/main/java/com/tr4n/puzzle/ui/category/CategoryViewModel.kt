package com.tr4n.puzzle.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.model.Preview
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.type.Category
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class CategoryViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    val selectedCategory = MutableLiveData<Category>()
    val categoryPreviews = MutableLiveData<List<Preview>>()

    fun getCategoryData(category: Category) = viewModelScope.launch {
        selectedCategory.value = category
        categoryPreviews.value = getCategoryReviews(category)
    }

    fun deleteChallenge(challenge: Challenge) = viewModelScope.launch {
        challengeRepository.deleteChallenge(challenge)
        selectedCategory.value?.let {
            categoryPreviews.value = getCategoryReviews(it)
        }
    }

    private suspend fun getCategoryReviews(category: Category): List<Preview> {
        val challenges = challengeRepository.getChallengesByCategory(category.value)
        return challenges.sortedByDescending { it.createdAt }
            .map { challenge ->
                Preview(challenge, ableDeleted = category == Category.MY_PUZZLES)
            }
    }
}
