package com.tr4n.puzzle.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.PreviewChallenge
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.type.Category
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class CategoryViewModel : BaseViewModel() {

    private val challengeRepository by inject<ChallengeRepository>()

    val selectedCategory = MutableLiveData<Category>()
    val categoryPreviews = MutableLiveData<List<PreviewChallenge>>()

    fun getChallengesData(category: Category) = viewModelScope.launch {
        selectedCategory.value = category
        val challenges = challengeRepository.getChallengesByCategory(category.value)
        val previews = challenges.sortedBy { it.createdAt }
            .map { challenge ->
                PreviewChallenge(challenge)
            }
        categoryPreviews.value = previews
    }
}
