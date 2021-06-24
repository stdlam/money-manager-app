package com.yplay.yspending.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yplay.yspending.R
import com.yplay.yspending.manager.NotificationManager
import com.yplay.yspending.manager.SharePreferenceManager

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val shouldFire = SharePreferenceManager.getStateReminder()
        if (shouldFire) {
            Log.d("ReminderReceiver", "onReceive hey, edit your spending")
            NotificationManager.push(context?.resources!!.getString(R.string.notification_content))
        }
        else {
            Log.d("ReminderReceiver", "onReceive hey, nothing")
        }
    }

}