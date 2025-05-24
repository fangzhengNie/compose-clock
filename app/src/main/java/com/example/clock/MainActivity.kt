package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockApp()
        }
    }
}

@Composable
fun ClockApp() {
    var time by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            time = getCurrentTime()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = time, fontSize = 48.sp, color = MaterialTheme.colorScheme.primary)
    }
}

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}
