package com.alexjlockwood.beesandbombs.demos.utils

import android.graphics.Path
import android.view.animation.PathInterpolator
import androidx.compose.animation.core.Easing

class PathEasing(path: Path) : Easing {

    constructor(pathData: String) : this(pathData.asAndroidPath())

    private val pathInterpolator = PathInterpolator(path)

    override fun transform(fraction: Float) = pathInterpolator.getInterpolation(fraction)
}