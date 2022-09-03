package com.tr4n.puzzle.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.tr4n.puzzle.R

abstract class BaseDialogFragment<VB : ViewDataBinding, VM : BaseViewModel> : DialogFragment() {

    protected lateinit var viewBD: VB

    @get:LayoutRes
    protected abstract val layoutResId: Int

    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewBD = DataBindingUtil.inflate(inflater, layoutResId, container, false) as VB
        return viewBD.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupWindowAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setBindingVariables()
        initData()
        observe()
    }

    open fun setupWindowAnimation() {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    /**
     * Set value for binding variables
     */
    open fun setBindingVariables() {}

    /**
     * Init your views, when they are ready.
     */
    open fun setUpView() {}

    /**
     * Observe the data change from your viewModel.
     */
    open fun observe() {
        clearOldObservers()
    }

    /**
     * Optional, To remove all unnecessary old observers before register the new.
     */
    open fun clearOldObservers() {}

    /**
     * Optional, request the data of screen on the first time.
     */
    open fun initData() {}

    override fun onStart() {
        super.onStart()
        setBackgroundTransparent()
    }

    protected fun navigateDirection(directions: NavDirections) {
        findNavController().navigate(directions)
    }

    private fun setBackgroundTransparent() {
        dialog?.window?.run {
            setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
