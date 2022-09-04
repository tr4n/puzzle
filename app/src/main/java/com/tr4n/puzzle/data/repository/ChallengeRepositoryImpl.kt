package com.tr4n.puzzle.data.repository

import com.tr4n.puzzle.data.database.AppDatabase
import com.tr4n.puzzle.data.model.Challenge

class ChallengeRepositoryImpl(
    private val database: AppDatabase
) : ChallengeRepository {

    override suspend fun getChallenges(): List<Challenge> {
        return database.challengeDao().getAll()
    }

    override suspend fun addChallenge(challenge: Challenge) {
        database.challengeDao().insert(challenge)
    }

    override suspend fun addChallenges(challenges: List<Challenge>) {
        database.challengeDao().insertAll(challenges)
    }

    override suspend fun saveChallenge(challenge: Challenge) {
        database.challengeDao().update(challenge)
    }

    override suspend fun getChallengesByCategory(categoryValue: Int): List<Challenge> {
        return database.challengeDao().getChallengesByCategory(categoryValue)
    }

    override suspend fun deleteChallenge(challenge: Challenge) {
        database.challengeDao().delete(challenge)
    }
}
