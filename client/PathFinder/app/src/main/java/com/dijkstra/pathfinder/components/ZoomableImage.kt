package com.dijkstra.pathfinder.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dijkstra.pathfinder.ui.theme.IconColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private const val TAG = "ZoomableImage_SDR"

@ExperimentalFoundationApi
@Composable
fun ZoomableImage(
    model: Any,
    contentDescription: String? = null,
) {
    var angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val animatedZoom by animateFloatAsState(
        targetValue = zoom,
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
                onDoubleClick = {
                    if ((zoom == 1f && (angle != 0f || offsetX != 0f || offsetY != 0f)) || (zoom != 1f)) {
                        zoom = 1f
                        angle = 0f
                        offsetX = 0f
                        offsetY = 0f
                    } else {
                        zoom = 2f
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .graphicsLayer(
                    scaleX = animatedZoom,
                    scaleY = animatedZoom,
                    rotationZ = angle
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                            angle += gestureRotate
                            zoom = (zoom * gestureZoom).coerceIn(1F..4F)
                            if (zoom > 1) {
                                val x = (pan.x * zoom)
                                val y = (pan.y * zoom)
                                val angleRad = angle * PI / 180.0

                                offsetX =
                                    (offsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                        -(screenWidth * zoom)..(screenWidth * zoom)
                                    )
                                offsetY =
                                    (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                        -(screenHeight * zoom)..(screenHeight * zoom)
                                    )
                            } else {
                                offsetX = 0F
                                offsetY = 0F
                            }
                        }
                    )
                }
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}