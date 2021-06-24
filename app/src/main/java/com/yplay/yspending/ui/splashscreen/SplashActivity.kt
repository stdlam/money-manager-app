package com.yplay.yspending.ui.splashscreen

import android.os.Bundle
import com.yplay.yspending.R
import com.yplay.yspending.ui.root.RootActivity

class SplashActivity : RootActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        var fragment: SplashFragment? = supportFragmentManager.findFragmentById(R.id.content) as SplashFragment?
        if (fragment == null) {
            fragment = SplashFragment.newInstance()
            addFragment(fragment, R.id.content)
        }
    }
}