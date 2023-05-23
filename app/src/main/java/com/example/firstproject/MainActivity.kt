package com.example.firstproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstproject.ui.theme.FirstProjectTheme

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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (starColor != Color.Yellow.value) {
                Image(
                    painter = painterResource(id = R.drawable.icon_start_full_24x24),
                    contentDescription = "icon starts",
                    colorFilter = ColorFilter.tint(Color(starColor))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.icon_start_full_24x24),
                    contentDescription = "icon starts",
                )
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "test back icon",
                Modifier.clickable {
                    Log.d("J", "right")
                    starColor = Color.Red.value
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
        Row {
            Text(text = message.value)
        }
    }

}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(message = Message("preview test", "test 101"))
}
