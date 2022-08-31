package com.tr4n.puzzle.extension

fun <T> Iterable<T>.replace(newItem: T, predicate: (T) -> Boolean) = map {
    if (predicate(it)) newItem else it
}

fun <T> Iterable<T>.replaceIndexed(newItem: T, predicate: (Int, T) -> Boolean) =
    mapIndexed { index, item ->
        if (predicate(index, item)) newItem else item
    }

fun <T> Iterable<T>.remove(predicate: (T) -> Boolean) = filter { !predicate(it) }

fun <T> Iterable<T>.sumIndexOf(selector: (index: Int, item: T) -> Int): Int {
    var sum = 0
    for ((index, element) in withIndex()) {
        sum += selector(index, element)
    }
    return sum
}

fun <T> Iterable<T>.indexAll(predicate: (index: Int, item: T) -> Boolean): Boolean {
    for ((index, element) in withIndex()) {
        if (!predicate(index, element)) {
            return false
        }
    }
    return true
}

fun <T> Iterable<T>.indexAny(predicate: (index: Int, item: T) -> Boolean): Boolean {
    for ((index, element) in withIndex()) {
        if (predicate(index, element)) {
            return true
        }
    }
    return false
}

fun <T> Iterable<T>.firstIndexOf(predicate: (index: Int, item: T) -> Boolean): Int {
    for ((index, element) in withIndex()) {
        if (predicate(index, element)) {
            return index
        }
    }
    return -1
}

fun <T> Iterable<T>.indexCount(predicate: (index: Int, item: T) -> Boolean): Int {
    var count = 0
    for ((index, element) in withIndex()) {
        if (predicate(index, element)) {
            count++
        }
    }
    return count
}
