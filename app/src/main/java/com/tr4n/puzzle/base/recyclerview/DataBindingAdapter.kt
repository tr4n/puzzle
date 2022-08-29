package com.tr4n.puzzle.base.recyclerview

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBindingAdapter<T>(
    diffUtilCallback: DiffUtil.ItemCallback<T> = BaseDiffUtilItemCallback()
) : ListAdapter<T, DataBindingViewHolder<ViewDataBinding>>(diffUtilCallback), BindAbleAdapter<T> {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<ViewDataBinding> {
        val binding = createBinding(parent, viewType)
        return DataBindingViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    override fun onBindViewHolder(holder: DataBindingViewHolder<ViewDataBinding>, position: Int) {
        bind(holder, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(
        holder: DataBindingViewHolder<ViewDataBinding>,
        item: T,
        position: Int
    )

    override fun submitList(list: MutableList<T>?) {
        super.submitList(list ?: emptyList())
    }

    override fun setItems(items: List<T>) {
        submitList(items.toMutableList())
    }
}

