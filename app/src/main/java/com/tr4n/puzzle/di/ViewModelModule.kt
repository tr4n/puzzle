package com.tr4n.puzzle.di

import com.tr4n.puzzle.ui.game.GameViewModel
import com.tr4n.puzzle.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { GameViewModel() }
}
