package com.yplay.yspending.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import androidx.core.app.NotificationCompat
import com.yplay.yspending.R
import com.yplay.yspending.ui.splashscreen.SplashActivity

object NotificationManager {
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var mContext: Context
    private const val mNotificationChannel = "com.yplay.yspending.manager"

    fun init(context: Context) {
        mContext = context
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(mNotificationChannel, "channelName", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = mContext.resources.getColor(R.color.colorPrimary)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(true)
            mNotificationManager.createNotificationChannel(channel)

        }
    }

    fun push(content: String) {
        val notifyIntent = Intent(mContext, SplashActivity::class.java)
        val pIntent = PendingIntent.getActivity(mContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notify = NotificationCompat.Builder(mContext, mNotificationChannel)
            .setContentTitle(mContext.resources.getString(R.string.reminder))
            .setContentText(Html.fromHtml(content))
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setChannelId(mNotificationChannel)
            .build()
        mNotificationManager.notify((System.currentTimeMillis() % 100000).toInt(), notify)
    }
}