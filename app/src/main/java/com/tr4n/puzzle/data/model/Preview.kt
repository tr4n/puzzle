package com.tr4n.puzzle.data.model

data class Preview(
    val challenge: Challenge = Challenge(),
    var title: String = "",
    var moreCount: Int = 0,
    var ableDeleted: Boolean = false
)
