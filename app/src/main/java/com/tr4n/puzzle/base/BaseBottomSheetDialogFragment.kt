package com.tr4n.puzzle.base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<VB : ViewDataBinding, VM : BaseViewModel> :
    BottomSheetDialogFragment() {

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { setupBottomSheet(it) }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        setBindingVariables()
        initData()
        observe()
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

    fun show(manager: FragmentManager?) {
        manager ?: return
        super.show(manager, null)
    }

    protected open fun setupBottomSheet(dialogInterface: DialogInterface) {
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
        dialogInterface.window?.navigationBarColor = Color.WHITE
//        val coordinatorLayout = bottomSheet?.parent as CoordinatorLayout
//        BottomSheetBehavior.from(bottomSheet).apply {
//            peekHeight = bottomSheet.height
//            isHideable = false
//        }
//        coordinatorLayout.parent.requestLayout()
    }
}
