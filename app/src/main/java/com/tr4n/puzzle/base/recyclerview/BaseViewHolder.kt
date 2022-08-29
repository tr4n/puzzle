package com.tr4n.puzzle.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T, VB : ViewDataBinding>(
    val binding: VB
) : RecyclerView.ViewHolder(binding.root) {

    protected var itemData: T? = null
        private set

    protected fun <V : VB> viewBinding() = binding as? V

    init {
        itemView.setOnClickListener {
            itemData?.let(::onItemClickListener)
        }
    }

    open fun bind(itemData: T) {
        this.itemData = itemData
    }

    open fun onItemClickListener(itemData: T) {
    }
}
