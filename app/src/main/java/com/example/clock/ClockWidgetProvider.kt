package com.example.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_clock)

            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            views.setTextViewText(R.id.widget_time, currentTime)

            appWidgetManager.updateAppWidget(id, views)
        }
        val intent= Intent(context,ClockWidgetProvider::class.java)
        intent.action=AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds)

        val pendingIntent=PendingIntent.getBroadcast(
            context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager=context.getSystemService(Context.ALARM_SERVICE)as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            60*1000,
            pendingIntent
        )
    }
}
