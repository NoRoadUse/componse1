package com.example.firstproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstproject.ui.theme.FirstProjectTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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
        Column(modifier = Modifier.padding(8.dp)) {
            var starResource by remember {
                mutableStateOf(R.drawable.icon_start_hollow_24x24)
            }
            var starColor by remember { mutableStateOf(Color.Yellow) }
            var starState by remember { mutableStateOf(false) }
            var isShowDefault by remember { mutableStateOf(false) }

            val interactionSource = MutableInteractionSource()
            val coroutineScope = rememberCoroutineScope()
            var currentRotation by remember { mutableStateOf(0f) }
            val rotation = remember { Animatable(currentRotation) }



            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = starResource),
                    contentDescription = "icon starts",
                    colorFilter = ColorFilter.tint(starColor),
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(currentRotation)
                        .clickable(interactionSource = interactionSource, indication = null) {
                            isShowDefault = true
                            starState = starState.not()
                            starResource =
                                if (starState) R.drawable.icon_start_full_24x24 else R.drawable.icon_start_hollow_24x24
                        },
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val interactionSource = MutableInteractionSource()
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
                AnimatedVisibility(
                    visible = isShowDefault,
                    enter = fadeIn(initialAlpha = 0.4f),
                    exit = fadeOut(animationSpec = tween(250))
                ) {
                    Text(
                        text = message.value,
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            starColor = Color.Yellow
                            currentRotation = 0f
                            starState = false
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
                colorBox(Color.Blue, colorCallback = {
                    starColor = it
                    isShowDefault = true
                })
                colorBox(Color.Green, colorCallback = {
                    starColor = it
                    isShowDefault = true
                })
                colorBox(Color.Red, colorCallback = {
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
fun colorBox(color: Color, colorCallback: (Color) -> Unit) {
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
            })
}