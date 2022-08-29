package com.tr4n.puzzle.base.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class DataBindingViewHolder<out T : ViewDataBinding>(
    val binding: T
) : RecyclerView.ViewHolder(binding.root)
