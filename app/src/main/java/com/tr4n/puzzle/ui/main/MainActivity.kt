package com.tr4n.puzzle.ui.main

import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseActivity
import com.tr4n.puzzle.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResource: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by inject()

    override fun initComponents() {
        initNavigation()
        setupTheme()
    }

    private fun initNavigation(){
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
    }

    @Suppress("DEPRECATION")
    private fun setupTheme() {
        window.apply {
            val winParams = this.attributes
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
            attributes = winParams
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        ViewCompat.setOnApplyWindowInsetsListener(viewBD.root) { _, insets: WindowInsetsCompat ->
            ViewCompat.onApplyWindowInsets(
                viewBD.root,
                insets.replaceSystemWindowInsets(
                    insets.systemWindowInsetLeft, 0,
                    insets.systemWindowInsetRight, insets.systemWindowInsetBottom
                )
            )
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}
