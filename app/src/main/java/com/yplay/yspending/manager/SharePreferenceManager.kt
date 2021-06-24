package com.yplay.yspending.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharePreferenceManager {
    private const val SHARED_PRE_REMINDER_HOUR_KEY = "SHARE_PRE_REMINDER_HOUR_KEY"
    private const val SHARED_PRE_REMINDER_MINUTE_KEY = "SHARE_PRE_REMINDER_MINUTE_KEY"
    private const val SHARED_PRE_REMINDER_STATE_KEY = "SHARE_PRE_REMINDER_STATE_KEY"

    private const val SHARED_PRE_STATE_REMINDER = "SHARED_PRE_REMINDER"
    private const val SHARED_PRE_HOUR_REMINDER = "SHARED_PRE_HOUR_REMINDER"
    private const val SHARED_PRE_MINUTE_REMINDER = "SHARED_PRE_MINUTE_REMINDER"

    private lateinit var mReminderStateSharedPreferences : SharedPreferences
    private lateinit var mReminderHourSharedPreferences : SharedPreferences
    private lateinit var mReminderMinuteSharedPreferences : SharedPreferences

    fun init(context: Context) {
        mReminderStateSharedPreferences = context.getSharedPreferences(SHARED_PRE_STATE_REMINDER, Context.MODE_PRIVATE)
        mReminderHourSharedPreferences = context.getSharedPreferences(SHARED_PRE_HOUR_REMINDER, Context.MODE_PRIVATE)
        mReminderMinuteSharedPreferences = context.getSharedPreferences(SHARED_PRE_MINUTE_REMINDER, Context.MODE_PRIVATE)
    }

    fun setReminder(hour: Int, minute: Int) {
        mReminderHourSharedPreferences.edit().putInt(SHARED_PRE_REMINDER_HOUR_KEY, hour).apply()
        mReminderMinuteSharedPreferences.edit().putInt(SHARED_PRE_REMINDER_MINUTE_KEY, minute).apply()
    }

    fun setReminderState(state: Boolean) {
        mReminderStateSharedPreferences.edit().putBoolean(SHARED_PRE_REMINDER_STATE_KEY, state).apply()
    }

    fun getHourReminder(): Int {
        return mReminderHourSharedPreferences.getInt(SHARED_PRE_REMINDER_HOUR_KEY, -1)
    }

    fun getMinuteReminder(): Int {
        return mReminderMinuteSharedPreferences.getInt(SHARED_PRE_REMINDER_MINUTE_KEY, -1)
    }

    fun getStateReminder(): Boolean {
        return mReminderStateSharedPreferences.getBoolean(SHARED_PRE_REMINDER_STATE_KEY, false)
    }
}