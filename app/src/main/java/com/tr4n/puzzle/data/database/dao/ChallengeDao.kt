package com.tr4n.puzzle.data.database.dao

import androidx.room.*
import com.tr4n.puzzle.data.model.Challenge

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenge")
    suspend fun getAll(): List<Challenge>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(challenge: Challenge)

    @Update
    suspend fun update(challenge: Challenge)

    @Delete
    suspend fun delete(challenge: Challenge)
}
