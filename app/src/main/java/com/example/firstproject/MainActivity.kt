package com.example.firstproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(message = Message("johnathan", "Default"))
        }
    }
}

data class Message(
    val name: String, val value: String
)

@Composable
fun MessageCard(message: Message) {

    Card(Modifier.padding(8.dp)) {
        val star =
            Rect(Offset(430f, 44f), Size(80.dp.value, 80.dp.value))

        val box1 =
            Rect(Offset(127f, 374f), Size(80.dp.value, 80.dp.value))
        val box2 =
            Rect(Offset(430f, 374f), Size(80.dp.value, 80.dp.value))
        val box3 =
            Rect(Offset(733f, 374f), Size(80.dp.value, 80.dp.value))


        Column(modifier = Modifier.padding(8.dp)) {
            var starResource by remember {
                mutableStateOf(R.drawable.icon_start_hollow_24x24)
            }
            var starColor by remember { mutableStateOf(Color.Yellow) }
            var starRefresh by remember { mutableStateOf(false) }
            var isShowDefault by remember { mutableStateOf(false) }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            val interactionSource = MutableInteractionSource()
            val coroutineScope = rememberCoroutineScope()
            var currentRotation by remember { mutableStateOf(0f) }
            val rotation = remember { Animatable(currentRotation) }
            val alpha: Float by animateFloatAsState(if (isShowDefault) 1f else 0f)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        this.rotationZ = currentRotation
                    }, horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = starResource),
                    contentDescription = "icon starts",
                    colorFilter = if (isShowDefault && starColor != Color.Yellow) ColorFilter.tint(
                        starColor
                    ) else null,
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(currentRotation)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            isShowDefault = true
                            starRefresh = false
                            starResource =
                                if (starResource == R.drawable.icon_start_hollow_24x24) R.drawable.icon_start_full_24x24 else R.drawable.icon_start_hollow_24x24
                            starRefresh = true
                        }
//                        .onGloballyPositioned { coordinates ->
//                            Log.e("test", "star ${coordinates.positionInRoot()}")
//                        }
                        .offset {
                            IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDragEnd = {
                                offsetX = 0f
                                offsetY = 0f
                            }, onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                                isShowDefault = true

                                Log.d("", "$offsetX $offsetY")

                                val starMove = star.translate(Offset(offsetX, offsetY))
                                starColor = when {
                                    starMove.overlaps(box1) -> {
                                        Color.Blue
                                    }

                                    starMove.overlaps(box2) -> {
                                        Color.Green
                                    }

                                    starMove.overlaps(box3) -> {
                                        Color.Red
                                    }

                                    else -> {
                                        starColor
                                    }
                                }
                            })
                        },
                )


            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                    contentDescription = "test back icon",
                    Modifier.clickable(interactionSource = interactionSource, indication = null) {
                        Log.d("J", "right")
                        isShowDefault = true
                        coroutineScope.launch {
                            rotation.animateTo(
                                currentRotation - 30f,
                                tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                            ) {
                                currentRotation = value
                            }
                        }
                    }
                )
                if (isShowDefault) {
                    Text(
                        text = message.value,
                        modifier = Modifier
                            .graphicsLayer {
                                this.alpha = alpha

                            }
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                starColor = Color.Yellow
                                currentRotation = 0f
                                starResource = R.drawable.icon_start_hollow_24x24
                                isShowDefault = false
                            })
                }

                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    contentDescription = "test back icon",
                    Modifier.clickable {
                        Log.d("J", "left")
                        isShowDefault = true
                        coroutineScope.launch {
                            rotation.animateTo(
                                currentRotation + 30f,
                                tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                            ) {
                                currentRotation = value
                            }
                        }
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorBox(Color.Blue, colorCallback = {
                    starColor = it
                    isShowDefault = true
                })
                ColorBox(Color.Green, colorCallback = {
                    starColor = it
                    isShowDefault = true
                })
                ColorBox(Color.Red, colorCallback = {
                    starColor = it
                    isShowDefault = true
                })
            }
        }
    }

}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(message = Message("preview test", "test 101"))
}

@Composable
fun ColorBox(color: Color, colorCallback: (Color) -> Unit) {
    val interactionSource = MutableInteractionSource()
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .size(80.dp)
            .background(color = color)
            .border(BorderStroke(2.dp, Color.Black))
            .border(BorderStroke(8.dp, Color.White))
            .clickable(interactionSource = interactionSource, indication = null) {
                coroutineScope.launch {
                    colorCallback.invoke(color)
                }
            }
            .onGloballyPositioned { coordinates ->
                Log.e("test", "${coordinates.positionInRoot().x} ${coordinates.positionInRoot().y}")
            })
}