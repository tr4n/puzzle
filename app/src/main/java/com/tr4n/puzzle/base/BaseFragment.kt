package com.tr4n.puzzle.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tr4n.puzzle.di.App.context
import com.tr4n.puzzle.extension.showSnackBar
import com.tr4n.puzzle.extension.showToast
import com.tr4n.puzzle.util.ConnectionType

abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel> : Fragment() {

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
        if (::viewBD.isInitialized.not()) {
            viewBD = DataBindingUtil.inflate(inflater, layoutResId, container, false) as VB
        }
        return viewBD.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindingVariables()
        setUpView()
        initData()
        observe()
    }

    /**
     * Set value for binding variables
     */
    open fun setBindingVariables() {
        viewBD.apply {
            root.isClickable = true
            lifecycleOwner = viewLifecycleOwner
        }
    }

    /**
     * Init your views, when they are ready.
     */
    open fun setUpView() {
    }

    /**
     * Observe the data change from your viewModel.
     */
    open fun observe() {
        clearOldObservers()
        viewModel.messageToast.observe(viewLifecycleOwner) {
            context?.showToast(it)
        }
        viewModel.messageSnackBar.observe(viewLifecycleOwner) {
            view?.showSnackBar(it)
        }
        viewModel.throwable.observe(viewLifecycleOwner) {
            it.printStackTrace()
            //     view?.showSnackBar(it.message.toString())
//            context?.showNotificationDialog {
//                title(R.string.error)
//                message(it.message.toString())
//                midButton(R.string.ok)
//            }
        }
    }

    /**
     * Optional, To remove all unnecessary old observers before register the new.
     */
    open fun clearOldObservers() {}

    /**
     * Optional, request the data of screen on the first time.
     */
    open fun initData() {}

    protected fun navigate(directions: NavDirections, navOptions: NavOptions? = null) {
        navOptions?.let {
            findNavController().navigate(directions, navOptions)
        } ?: findNavController().navigate(directions)
    }

    protected fun navigate(
        @IdRes resId: Int,
        bundle: Bundle? = null,
        navOptions: NavOptions? = null
    ) {
        findNavController().navigate(resId, bundle, navOptions)
    }

    open fun haveInternetConnectionBack() {
        view?.showSnackBar("Network available")
    }

    open fun lostInternetConnection() {
        view?.showSnackBar("No Internet")
    }

    protected fun setTextColorStatusBar(isTextDark: Boolean) {
        activity?.window?.decorView?.systemUiVisibility = if (isTextDark) {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }
}
