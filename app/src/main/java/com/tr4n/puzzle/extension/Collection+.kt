package com.tr4n.puzzle.extension

fun <T> Iterable<T>.replace(newItem: T, predicate: (T) -> Boolean) = map {
    if (predicate(it)) newItem else it
}

fun <T> Iterable<T>.replaceIndexed(newItem: T, predicate: (Int, T) -> Boolean) =
    mapIndexed { index, item ->
        if (predicate(index, item)) newItem else item
    }

fun <T> Iterable<T>.remove(predicate: (T) -> Boolean) = filter { !predicate(it) }

