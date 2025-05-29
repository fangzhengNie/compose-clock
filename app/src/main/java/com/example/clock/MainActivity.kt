package com.example.clock

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        setContent {
            ClockApp()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }

    }
}

@Composable
fun ClockApp() {
    var time by remember { mutableStateOf(getCurrentTime()) }
    var date by remember {mutableStateOf(getCurrentDate())}
    val context= LocalContext.current
    var showTimePicker by remember { mutableStateOf(false) }
    var showAlarmList by remember{ mutableStateOf(false) }
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(modifier = Modifier.height(120.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally){
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
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Button(
                    onClick = {showTimePicker=true},
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("设置闹钟")
                }
                Button(
                    onClick = {
                        val intent=Intent(context,AlarmListActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("查看所有闹钟")
                }
            }
        }
        //Text(text = time, fontSize = 48.sp, color = MaterialTheme.colorScheme.primary)
    }
    if(showTimePicker){
        LaunchedEffect(Unit) {
            val calendar=Calendar.getInstance()
            TimePickerDialog(
                context,
                {
                        _,hour,minute->
                    showTimePicker=false
                    setAlarm(context,hour,minute)
                    saveAlarmTime(context, hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }
    /*
    if (showAlarmList) {
        val alarms = getAllAlarms(context)
        Column(modifier = Modifier.padding(top = 16.dp)) {
            if (alarms.isEmpty()) {
                Text("暂无设置的闹钟", color = MaterialTheme.colorScheme.onPrimary)
            } else {
                alarms.forEach { alarm ->
                    Text(text = alarm, fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
     */
}

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    //formatter.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    return formatter.format(Date())
}
fun getCurrentDate():String{
    val formatter=SimpleDateFormat("yyyy年MM月dd日 EEEE",Locale.CHINA)
    //formatter.timeZone=TimeZone.getTimeZone("Asia/Shanghai")
    return formatter.format(Date())
}
fun setAlarm(context: Context, hour: Int, minute: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "当前系统未允许设置精确闹钟", Toast.LENGTH_SHORT).show()
            return
        }
    }
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, 0, intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (before(Calendar.getInstance())) {
            add(Calendar.DATE, 1)
        }
    }

    try {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Toast.makeText(context, "闹钟设置成功", Toast.LENGTH_SHORT).show()
    } catch (e: SecurityException) {
        e.printStackTrace()
        Toast.makeText(context, "设置闹钟失败，权限不足", Toast.LENGTH_SHORT).show()
    }
}
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Alarm Notifications"
        val descriptionText = "闹钟提醒通知"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("alarm_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
fun saveAlarmTime(context: Context, hour: Int, minute: Int) {
    val sharedPrefs = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    val key = System.currentTimeMillis().toString()
    editor.putString(key, String.format("%02d:%02d", hour, minute))
    editor.apply()
}

fun getAllAlarms(context: Context): Map<String, String> {
    val sharedPrefs = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    return sharedPrefs.all.mapValues { it.value.toString() }
}

fun deleteAlarm(context: Context, key: String) {
    val sharedPrefs = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)
    sharedPrefs.edit().remove(key).apply()
}


