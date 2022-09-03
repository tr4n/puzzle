package com.tr4n.puzzle.di

import com.tr4n.puzzle.ui.camera.CameraViewModel
import com.tr4n.puzzle.ui.dashboard.DashboardViewModel
import com.tr4n.puzzle.ui.game.GameViewModel
import com.tr4n.puzzle.ui.main.MainViewModel
import com.tr4n.puzzle.ui.review.ReviewImageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { DashboardViewModel() }
    viewModel { CameraViewModel() }
    viewModel { ReviewImageViewModel() }
    viewModel { GameViewModel() }
}
