package com.tr4n.puzzle.data.repository

import com.tr4n.puzzle.data.model.Challenge

interface ChallengeRepository {

    suspend fun getChallenges(): List<Challenge>

    suspend fun addChallenge(challenge: Challenge)

    suspend fun saveChallenge(challenge: Challenge)
}
