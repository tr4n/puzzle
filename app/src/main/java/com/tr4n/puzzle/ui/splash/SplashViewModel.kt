package com.tr4n.puzzle.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.type.Category
import com.tr4n.puzzle.util.prefs.SharedPrefKey
import com.tr4n.puzzle.util.prefs.SharedPrefs
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class SplashViewModel : BaseViewModel() {

    private val challengeRepository: ChallengeRepository by inject()

    val isDataInitialized = MutableLiveData<Boolean>()

    fun initData() = viewModelScope.launch {
        val lastUpdatedTime = SharedPrefs[SharedPrefKey.LAST_UPDATED_TIME, 0L]
        if (lastUpdatedTime > 0) {
            isDataInitialized.value = true
            return@launch
        }
        val challenges = Category.values().flatMap { category ->
            category.offlineImages.mapIndexed { index, _ ->
                Challenge(
                    imageName = category.toImageName(index),
                    type = category.value
                )
            }
        }
        challengeRepository.addChallenges(challenges)
        SharedPrefs.put(SharedPrefKey.LAST_UPDATED_TIME, System.currentTimeMillis())
        isDataInitialized.value = true
    }
}
