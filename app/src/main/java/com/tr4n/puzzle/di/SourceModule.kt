package com.tr4n.puzzle.di

import com.tr4n.puzzle.data.database.AppDatabase
import com.tr4n.puzzle.data.repository.ChallengeRepository
import com.tr4n.puzzle.data.repository.ChallengeRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sourceModule = module {

    single {
        AppDatabase.build(androidContext())
    }


    single<ChallengeRepository> {
        ChallengeRepositoryImpl(get())
    }
}
