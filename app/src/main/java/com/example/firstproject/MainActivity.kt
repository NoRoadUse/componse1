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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
            MessageCard(message = Message("johnathan", "test 123"))
        }
    }
}

data class Message(
    val name: String, val value: String
)

@Composable
fun MessageCard(message: Message) {
    Column {
        var starColor by remember { mutableStateOf(Color.Yellow.value) }
        var starState by remember { mutableStateOf(false) }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            StarComponent(starState = starState, onStarStateChanged = { starState = it })
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
//                    starColor = Color.Red.value

                }
            )
            Text(text = message.name)
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "test back icon",
                Modifier.clickable {
                    Log.d("J", "left")
                    starColor = Color.Blue.value
                }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            colorBox(Color.Blue)
            colorBox(Color.Yellow)
            colorBox(Color.Red)
        }
    }

}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(message = Message("preview test", "test 101"))
}

@Composable
fun StarComponent(starState: Boolean, onStarStateChanged: (Boolean) -> Unit) {

    val interactionSource = MutableInteractionSource()
    val coroutineScope = rememberCoroutineScope()
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    Image(
        painter = painterResource(id = if (starState) R.drawable.icon_start_hollow_24x24 else R.drawable.icon_start_full_24x24),
        contentDescription = "icon starts",
        modifier = Modifier
            .size(80.dp)
            .rotate(currentRotation)
            .clickable(interactionSource = interactionSource, indication = null) {
                onStarStateChanged.invoke(starState.not())
                coroutineScope.launch {
                    rotation.animateTo(
                        currentRotation + 30f,
                        tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                    ) {
                        currentRotation = value
                    }
                }
            },
    )
}

@Composable
fun colorBox(color: Color) {
    val interactionSource = MutableInteractionSource()
    val coroutineScope = rememberCoroutineScope()
    val test = remember { Animatable(color) }

    Box(
        Modifier
            .size(24.dp)
            .background(color = test.value)
            .clickable(interactionSource = interactionSource, indication = null) {
                coroutineScope.launch {
                    test.animateTo(Color.Black, animationSpec = tween(1000))
                }
            })
}