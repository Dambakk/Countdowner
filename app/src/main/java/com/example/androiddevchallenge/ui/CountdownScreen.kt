package com.example.androiddevchallenge.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.CountdownViewModel
import com.example.androiddevchallenge.TimeLeftModel
import com.example.androiddevchallenge.ui.theme.MyTheme

@Composable
@Preview
fun CountdownScreenPreview() {
    MyTheme {
        val vm = CountdownViewModel()
        CountdownScreen(vm)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountdownScreen(viewModel: CountdownViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        var selectedTime by remember { mutableStateOf(TimeLeftModel(60)) }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Button(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .animateContentSize(),
                onClick = {
                    if (viewModel.timeLeft == null) {
                        viewModel.startCountdown(selectedTime.totalSeconds)
                    } else {
                        viewModel.stopTimer()
                    }
                },
            ) {
                Text(
                    text = if (viewModel.timeLeft == null) "Start countdown (${selectedTime})" else "Stop"
                )
            }
            InputSlider {
                selectedTime = TimeLeftModel(it.toLong())
            }

            Spacer(modifier = Modifier.height(40.dp))

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                CountdownCircleWrapper(
                    circleColor = Color.Yellow,
                    degreesLeft = viewModel.timeLeft?.totalSeconds?.let { (it.toFloat() / selectedTime.totalSeconds.toFloat()) * 360f }
                        ?: 360f,
                    modifier = Modifier.size((maxWidth * 0.9f))
                )

                CountdownCircleWrapper(
                    circleColor = Color.Green,
                    degreesLeft = viewModel.timeLeft?.secondsInMinute?.let { (it / 60f) * 360f }
                        ?: 360f,
                    modifier = Modifier.size((maxWidth * 0.7f))
                )

                CountdownCircleWrapper(
                    circleColor = Color.Blue,
                    degreesLeft = viewModel.timeLeft?.secondsInTen?.let { (it / 10f) * 360f }
                        ?: 360f,
                    modifier = Modifier.size((maxWidth * 0.5f))
                )

                androidx.compose.animation.AnimatedVisibility(
                    visible = viewModel.timeLeft != null,
                    enter = fadeIn(initialAlpha = 0.1f),
                    exit = fadeOut(),
                ) {
                    Box(contentAlignment = Alignment.Center) {

                        // Text background
                        CountdownCircleWrapper(
                            circleColor = Color.Black,
                            degreesLeft = 360f,
                            modifier = Modifier.size(this@BoxWithConstraints.maxWidth * 0.3f)
                        )

                        Text(
                            text = viewModel.timeLeft?.toString() ?: "",
                            color = Color.White,
                            style = MaterialTheme.typography.h4,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputSlider(onTimeSet: (seconds: Int) -> Unit) {
    val (sliderState, setValue) = remember { mutableStateOf(5f) }
    Slider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .wrapContentHeight(),
        value = sliderState,
        valueRange = 0f..120f,
        onValueChange = {
            setValue(it)
            onTimeSet(it.toInt())
        }
    )
}

@Composable
private fun CountdownCircleWrapper(
    circleColor: Color,
    degreesLeft: Float,
    modifier: Modifier
) {
    val degreesAnimatable by animateFloatAsState(targetValue = degreesLeft)
    CountdownCircle(
        degrees = degreesAnimatable.toInt(),
        circleColor = circleColor,
        modifier = modifier
    )
}


/**
 * Requires a modifier with size set.
 */
@Composable
fun CountdownCircle(
    degrees: Int,
    circleColor: Color,
    modifier: Modifier,
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val center = Offset(x = canvasWidth / 2, y = canvasHeight / 2)
        val radius = size.minDimension / 2
        val degreesToDraw = if (degrees == 0) 360f - 0.1f else 360f - degrees.toFloat()
        withTransform(transformBlock = {
            clipPath(clipOp = ClipOp.Intersect, path = Path().apply {
                val rect = Rect(center, radius)
                addRect(rect)
                moveTo(canvasWidth / 2, canvasHeight / 2)
                arcTo(rect, -90f, degreesToDraw, forceMoveTo = false)
//                arcTo(rect, -90f, degrees.toFloat(), forceMoveTo = false) // This one is opposite of what i want; i.e. increases
//                arcTo(rect, -90f, 360f - degrees, forceMoveTo = false)
//                arcTo(rect, -90f - degrees, 360f - degrees, forceMoveTo = false)
            })
        }) {
            drawCircle(
                color = circleColor,
                center = center,
                radius = radius
            )
        }
    }
}