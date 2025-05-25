package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
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
    var date by remember {mutableStateOf(getCurrentDate())}
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            time = getCurrentTime()
            date=getCurrentDate()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush= Brush.verticalGradient(
                    colors=listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha=0.8f),
                        MaterialTheme.colorScheme.secondary.copy(alpha=0.8f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text=time,
                fontSize=64.sp,
                color=MaterialTheme.colorScheme.onPrimary,
                style=MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text=date,
                fontSize = 24.sp,
                color=MaterialTheme.colorScheme.onPrimary.copy(alpha=0.8f),
                style=MaterialTheme.typography.bodyLarge
            )
        }
        //Text(text = time, fontSize = 48.sp, color = MaterialTheme.colorScheme.primary)
    }
}

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    return formatter.format(Date())
}
fun getCurrentDate():String{
    val formatter=SimpleDateFormat("yyyy年MM月dd日 EEEE",Locale.CHINA)
    formatter.timeZone=TimeZone.getTimeZone("Asia/Shanghai")
    return formatter.format(Date())
}
