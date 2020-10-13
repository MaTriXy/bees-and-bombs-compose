package com.alexjlockwood.beesandbombs.demos

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import com.alexjlockwood.beesandbombs.demos.utils.CatmullRom
import com.alexjlockwood.beesandbombs.demos.utils.PI
import com.alexjlockwood.beesandbombs.demos.utils.TWO_PI
import com.alexjlockwood.beesandbombs.demos.utils.dist
import com.alexjlockwood.beesandbombs.demos.utils.ease
import com.alexjlockwood.beesandbombs.demos.utils.lerp
import com.alexjlockwood.beesandbombs.demos.utils.map
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val N = 360
private const val NUM_LINES = 18
private const val NUM_WAVES = 18
private const val LINE_LENGTH = 500f
private const val WAVE_HEIGHT = 20f
private const val SPACING = 27f
private const val CURL_AMOUNT = 12f

/**
 * Creates a composable wave square animation.
 */
@Composable
fun WaveSquare(modifier: Modifier = Modifier) {
    val animatedProgress = animatedFloat(0f)
    onActive {
        animatedProgress.animateTo(
            targetValue = 1f,
            anim = repeatable(
                iterations = AnimationConstants.Infinite,
                animation = tween(durationMillis = 5000, easing = LinearEasing),
            ),
        )
    }

    val catmullRom = remember { CatmullRom() }

    val darkColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val t = animatedProgress.value

    Canvas(modifier = modifier) {
        withTransform({
            val scale = size.minDimension / LINE_LENGTH
            scale(scale, scale, Offset(center.x, center.y))
            translate(size.width / 2f, size.height / 2f)
        }, {
            for (l in 0 until NUM_LINES) {
                catmullRom.lineStart()
                for (n in 0 until N) {
                    val qq = n.toFloat() / (N - 1)
                    val phase = map(n.toFloat(), 0f, N - 1f, 0f, TWO_PI * NUM_WAVES) - TWO_PI * t
                    var x = lerp(-LINE_LENGTH / 2f, LINE_LENGTH / 2f, qq)
                    var y = (SPACING * (l - 0.5f * (NUM_LINES - 1)))

                    val amount = ease(map(cos(TWO_PI * t + atan2(x, y) - 0.01f * dist(x, y, 0f, 0f)), 1f, -1f, 0f, 1f))
                    y += 0.5f * WAVE_HEIGHT * sin(phase + PI * l) * amount - 0.2f * WAVE_HEIGHT * amount
                    x -= CURL_AMOUNT * cos(phase + PI * l) * amount

                    catmullRom.point(x, y)
                }
                catmullRom.lineEnd()
                drawPath(
                    path = catmullRom.path,
                    color = darkColor,
                    style = Stroke(width = 2f, miter = 1f),
                )
            }
        })
    }
}
