/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Modifier must contain specific size
 */
@Composable
fun Confetti(
    modifier: Modifier,
    numParticles: Int = 20,
) {
    BoxWithConstraints(modifier = modifier) {
        val widthRange = 0..(maxWidth.value.toInt())
        val heightRange = 3..20
        repeat(numParticles) {
            val randomX = widthRange.random()
            val randomY = heightRange.random()
            Particle(startPosition = Offset(randomX.dp.value, randomY.dp.value))
        }
    }
}

@Composable
private fun Particle(
    startPosition: Offset,
    color: Color = Color.Blue
) {
    Canvas(modifier = Modifier.size(20.dp, 5.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRect(
            color = color,
            topLeft = Offset(canvasWidth / 2, canvasHeight / 2),
            size = Size(canvasWidth, canvasHeight)
        )
    }
}
