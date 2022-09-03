package com.tr4n.puzzle.listener

import com.tr4n.puzzle.base.recyclerview.DataBindingListener

interface OnSimpleItemClick<T> : DataBindingListener {

    fun onClick(item: T) {}

    fun onClick(position: Int) {}

    fun onClick(item: T, position: Int) {}
}
