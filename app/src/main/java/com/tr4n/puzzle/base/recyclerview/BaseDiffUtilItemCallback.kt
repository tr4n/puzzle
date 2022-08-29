package com.tr4n.puzzle.base.recyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

@Suppress("WRONG_TYPE_PARAMETER_NULLABILITY_FOR_JAVA_OVERRIDE")
open class BaseDiffUtilItemCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        // Override this if your item have an id
        return oldItem === newItem
    }

    /**
     * 2 ways to make this happened
     *  - Simple way: make your [T] item is data class
     *  - Performance way: make your own [T] item's equals() & hashcode() methods
     */
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) =
        oldItem == newItem
}
