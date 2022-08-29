package com.tr4n.puzzle.base.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.tr4n.puzzle.BR

open class SimpleDataBindingAdapter<T>(
    @LayoutRes private val layoutId: Int,
    diffUtilCallback: DiffUtil.ItemCallback<T> = BaseDiffUtilItemCallback()
) : DataBindingAdapter<T>(diffUtilCallback) {

    var listener: DataBindingListener? = null

    override fun bind(holder: DataBindingViewHolder<ViewDataBinding>, item: T, position: Int) {
        holder.binding.apply {
//            setVariable(BR.item, item)
//            setVariable(BR.position, position)
//            setVariable(BR.isLast, position == itemCount - 1)
//            listener?.let {
//                setVariable(BR.listener, it)
//            }
            executePendingBindings()
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
    }
}

interface DataBindingListener
