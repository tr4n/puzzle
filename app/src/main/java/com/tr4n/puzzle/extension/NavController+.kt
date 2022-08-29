package com.tr4n.puzzle.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.tr4n.puzzle.R
import com.tr4n.puzzle.util.NavigationKeys

fun NavController.navigateWithAnim(directions: NavDirections) {
    val option = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
    navigate(directions, option)
}

fun NavController.navigateWithAnim(@IdRes resId: Int, bundle: Bundle? = null) {
    val option = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
    navigate(resId, bundle, option)
}

fun <T> NavController.getResultLiveData(@NavigationKeys key: String = NavigationKeys.DEFAULT) =
    currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> NavController.setResultLiveData(
    result: T,
    @NavigationKeys key: String = NavigationKeys.DEFAULT
) {
    previousBackStackEntry?.savedStateHandle?.set(key, result)
}
