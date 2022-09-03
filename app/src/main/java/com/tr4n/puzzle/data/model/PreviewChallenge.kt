package com.tr4n.puzzle.data.model

import android.graphics.Bitmap

data class PreviewChallenge(
    val challenge: Challenge = Challenge(),
    val preview: Bitmap? = null
)
