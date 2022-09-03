package com.tr4n.puzzle.data.type

import androidx.annotation.DrawableRes
import com.tr4n.puzzle.R

enum class Category(val value: Int, val keyword: String, val offlineImages: List<Int>) {
    ANIME(
        value = 1,
        keyword = "anime",
        offlineImages = listOf(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5,
            R.drawable.dog6,
            R.drawable.dog7,
            R.drawable.dog8,
            R.drawable.dog9,
            R.drawable.dog10,
            R.drawable.dog11
        ),
    ),
    CAT(
        value = 2,
        keyword = "cat",
        offlineImages = listOf(
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
            R.drawable.cat5,
            R.drawable.cat6,
            R.drawable.cat7,
            R.drawable.cat8,
            R.drawable.cat9,
            R.drawable.cat10,
            R.drawable.cat11,
            R.drawable.cat12,
        )
    ),
    CARTOON(
        value = 3,
        keyword = "cartoon",
        offlineImages = listOf(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5,
            R.drawable.dog6,
            R.drawable.dog7,
            R.drawable.dog8,
            R.drawable.dog9,
            R.drawable.dog10,
            R.drawable.dog11
        ),
    ),
    DOG(
        value = 4,
        keyword = "dog",
        offlineImages = listOf(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5,
            R.drawable.dog6,
            R.drawable.dog7,
            R.drawable.dog8,
            R.drawable.dog9,
            R.drawable.dog10,
            R.drawable.dog11
        ),
    ),
    FOOD(
        value = 5,
        keyword = "food",
        offlineImages = listOf(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5,
            R.drawable.dog6,
            R.drawable.dog7,
            R.drawable.dog8,
            R.drawable.dog9,
            R.drawable.dog10,
            R.drawable.dog11
        ),
    ),
    SPORT(
        value = 6,
        keyword = "sport",
        offlineImages = listOf(
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.dog3,
            R.drawable.dog4,
            R.drawable.dog5,
            R.drawable.dog6,
            R.drawable.dog7,
            R.drawable.dog8,
            R.drawable.dog9,
            R.drawable.dog10,
            R.drawable.dog11
        ),
    );

    fun toImageName(index: Int) = "${name}_${index}"

    companion object {

        fun fromValue(value: Int): Category? = values().find { it.value == value }

        @DrawableRes
        fun getImageDrawableRes(imageName: String): Int? {
            if (!imageName.matches("[A-Z]+_[\\d]+".toRegex())) return null

            val (name, index) = imageName.split("_").run {
                first() to last().toInt()
            }
            val category = Category.values().find { it.name == name } ?: return null
            return category.offlineImages.getOrNull(index)
        }
    }
}
