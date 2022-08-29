package com.tr4n.puzzle.util

import androidx.annotation.StringDef
import com.tr4n.puzzle.util.NavigationKeys.Companion.DEFAULT
import com.tr4n.puzzle.util.NavigationKeys.Companion.SELECT_ITEM_OPTION

@StringDef(DEFAULT, SELECT_ITEM_OPTION)
annotation class NavigationKeys {
    companion object {
        const val DEFAULT = "RESULT"
        const val SELECT_ITEM_OPTION = "SELECT_ITEM_OPTION"
    }
}
