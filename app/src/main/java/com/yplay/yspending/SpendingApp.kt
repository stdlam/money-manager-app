package com.yplay.yspending

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yplay.yspending.manager.NotificationManager
import com.yplay.yspending.manager.SharePreferenceManager

class SpendingApp : Application.ActivityLifecycleCallbacks, Application() {

    lateinit var applicationComponent: ApplicationComponent

    companion object {
        lateinit var instance: SpendingApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharePreferenceManager.init(this)
        NotificationManager.init(this)

        val applicationModule = ApplicationModule(this)
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(applicationModule)
            .build()
        applicationComponent.inject(this)
    }

    override fun onActivityPaused(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(activity: Activity) {
        TODO("Not yet implemented")
    }

}