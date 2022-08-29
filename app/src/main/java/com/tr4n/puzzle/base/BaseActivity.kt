package com.tr4n.puzzle.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tr4n.puzzle.extension.showSnackBar
import com.tr4n.puzzle.extension.showToast

abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> : Activity(), LifecycleOwner {

    @get:LayoutRes
    protected abstract val layoutResource: Int
    protected lateinit var viewBD: VB
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        setBindingVariables()
        initComponents()
        observeData()
    }

    private fun performDataBinding() {
        if (::viewBD.isInitialized.not()) {
            viewBD = DataBindingUtil.setContentView(this, layoutResource) as VB
            viewBD.apply {
                root.isClickable = true
                lifecycleOwner = this@BaseActivity
                executePendingBindings()
            }
        }
    }

    protected open fun setBindingVariables() {}

    protected abstract fun initComponents()

    protected open fun observeData() {
        viewModel.messageToast.observe(this, Observer {
            showToast(it)
        })
        viewModel.messageSnackBar.observe(this, Observer {
            findViewById<View>(android.R.id.content).showSnackBar(it)
        })
        viewModel.throwable.observe(this, Observer {
            it.printStackTrace()
            //           findViewById<View>(android.R.id.content).showSnackBar(it.message.toString())
//            showNotificationDialog {
//                title(R.string.error)
//                message(it.message.toString())
//                midButton(R.string.ok)
//            }
        })
    }
}
