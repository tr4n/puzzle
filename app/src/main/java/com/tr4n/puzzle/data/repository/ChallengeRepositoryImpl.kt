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

    override suspend fun saveChallenge(challenge: Challenge) {
        database.challengeDao().update(challenge)
    }
}
