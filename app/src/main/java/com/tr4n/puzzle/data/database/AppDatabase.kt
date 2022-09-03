package com.tr4n.puzzle.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tr4n.puzzle.data.database.AppDatabase.Companion.DATABASE_VERSION
import com.tr4n.puzzle.data.database.dao.ChallengeDao
import com.tr4n.puzzle.data.model.Challenge

@Database(
    entities = [
        Challenge::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun challengeDao(): ChallengeDao

    companion object {
        private const val DATABASE_NAME = "puzzle"
        const val DATABASE_VERSION = 1

        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                //     .createFromAsset(DATABASE_PATH)
                .fallbackToDestructiveMigration()
                .build()
    }
}
