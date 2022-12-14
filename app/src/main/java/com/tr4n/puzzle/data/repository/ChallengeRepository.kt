package com.tr4n.puzzle.data.repository

import com.tr4n.puzzle.data.model.Challenge

interface ChallengeRepository {

    suspend fun getChallenges(): List<Challenge>

    suspend fun getChallengesByCategory(categoryValue: Int): List<Challenge>

    suspend fun addChallenge(challenge: Challenge)

    suspend fun saveChallenge(challenge: Challenge)

    suspend fun addChallenges(challenges: List<Challenge>)

    suspend fun deleteChallenge(challenge: Challenge)
}
