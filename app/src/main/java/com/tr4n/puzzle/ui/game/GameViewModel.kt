package com.tr4n.puzzle.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseViewModel
import com.tr4n.puzzle.data.model.Challenge
import com.tr4n.puzzle.data.model.Puzzle
import com.tr4n.puzzle.data.model.State
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.type.Move
import com.tr4n.puzzle.di.App
import com.tr4n.puzzle.util.BitmapUtils
import com.tr4n.puzzle.util.BitmapUtils.cropCenter
import com.tr4n.puzzle.util.BitmapUtils.split
import com.tr4n.puzzle.util.FileUtils
import com.tr4n.puzzle.util.solution.PuzzleSolution
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import kotlin.math.max
import kotlin.math.min
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class GameViewModel : BaseViewModel() {

    private val challengeRepository: ChallengeRepository by inject()

    private var state = State()

    private var challenge = Challenge()

    private var originPuzzles = listOf<Puzzle>()

    val currentPuzzles = MutableLiveData<List<Puzzle>>()

    val stepCount = MutableLiveData(0)

    val isChallengeSaved = MutableLiveData<Boolean>()

    private var fullBitmap = BitmapUtils.decodeSampledBitmapFromResource(
        App.context.resources,
        R.drawable.sample
    ).cropCenter()

    fun initChallenge(challenge: Challenge) {
        this.challenge = challenge

        val file = FileUtils.getImageFile(challenge.imageName)
        fullBitmap = BitmapUtils.decodeSampledBitmapFromFile(file).cropCenter()
        originPuzzles = fullBitmap.split(challenge.size).mapIndexed { index, bitmap ->
            Puzzle(index, bitmap = bitmap)
        }
        updateNewState(State(challenge.puzzleIndexes))
        stepCount.value = 0
    }

    fun swipePuzzle(move: Move) {
        stepCount.value = (stepCount.value ?: 0) + 1
        updateNewState(state.move(move))
    }

    fun shuffle() {
        stepCount.value = 0
        var newState = state
        var previousMove: Move? = null
        repeat(challenge.size * 50) {
            val move = Move.values().filter { (it.value + 2) % 4 != previousMove?.value }.random()
            newState = newState.move(move)
            previousMove = move
        }
        updateNewState(newState)
    }

    @OptIn(ExperimentalTime::class)
    fun solve() {
        stepCount.value = 0
        viewModelScope.launch {
            loading.value = true
            val result = measureTimedValue {
                PuzzleSolution(state).search() ?: emptyList()
            }
            val solvedStates = result.value
            loading.value = false

            val timeDelayPerStep = max(min(6000L / solvedStates.size, 250L), 50L)

            for (state in solvedStates) {
                delay(timeDelayPerStep)
                updateNewState(state)
                stepCount.value = (stepCount.value ?: 0) + 1
            }
            messageSnackBar.value =
                if (solvedStates.isEmpty()) "None" else "${result.duration.toInt(DurationUnit.MILLISECONDS) / 1000f}s"
        }
    }

    fun saveChallenge() = viewModelScope.launch {
        loading.value = true
        challenge.apply {
            updatedAt = System.currentTimeMillis()
        }
        challengeRepository.addChallenge(challenge)
        loading.value = false
        isChallengeSaved.value = true
    }

    private fun updateNewState(newState: State) {
        state = newState
        challenge.state = newState.hashCode
        currentPuzzles.value = newState.puzzleIndexes.map {
            if (it == 0 && newState.isTarget) {
                originPuzzles[it].copy(show = true)
            } else {
                originPuzzles[it]
            }
        }
    }
}

