package com.setiaki.githubusersubmission2

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_TITLE = "extra_notification_title"
        const val EXTRA_MESSAGE = "extra_notification_message"

        const val TYPE_9AM_REPEATING_ALARM = "type_9am_repeating_alarm"
        private const val ID_9AM_REPEATING_ALARM = 101
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val id = if (type == TYPE_9AM_REPEATING_ALARM) ID_9AM_REPEATING_ALARM else null
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        if (id != null && title != null && message != null) {
            showAlarmNotification(context, id, title, message)
        }
    }

    fun set9AMRepeatingAlarm(context: Context?, title: String?, message: String?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(EXTRA_TYPE, TYPE_9AM_REPEATING_ALARM)
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_9AM_REPEATING_ALARM, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        val toastMessage =
            context.resources.getString(R.string.toast_message_repeating_alarm_activated)
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
    }


    fun cancel9AMRepeatingAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_9AM_REPEATING_ALARM
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)

        val toastMessage =
            context.resources?.getString(R.string.toast_message_repeating_alarm_deactivated)
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
    }

    private fun showAlarmNotification(context: Context?, id: Int, title: String?, message: String?) {
        val channelId = "channel_1"
        val channelName = "AlarmManager channel"

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent , 0)

        val notificationManagerCompat =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent ))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(id, notification)
    }
}