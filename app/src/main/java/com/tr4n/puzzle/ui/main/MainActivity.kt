package com.tr4n.puzzle.ui.main

import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.BaseActivity
import com.tr4n.puzzle.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResource: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by inject()

    override fun initComponents() {
        setupTheme()
    }

    @Suppress("DEPRECATION")
    private fun setupTheme() {
        // Root ViewGroup of my activity
        val root = viewBD.root

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->

            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onBackPressed() {
    }
}
