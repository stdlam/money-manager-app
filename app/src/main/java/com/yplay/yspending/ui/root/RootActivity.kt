package com.yplay.yspending.ui.root

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yplay.yspending.R
import com.yplay.yspending.ui.baseview.HomeBaseFragment

@SuppressLint("Registered")
open class RootActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
    }

    protected fun addFragment(fragment: Fragment, fragmentId: Int) {
        addFragment(fragment, null, fragmentId)
    }

    protected fun addSurfaceFragment(fragment: Fragment, fragmentId: Int, tag: String?) {
        addSurfaceFragment(fragment, tag, fragmentId)
    }

    private fun addFragment(fragment: Fragment, tag: String?, fragmentId: Int) {
        supportFragmentManager.beginTransaction().replace(fragmentId, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun addSurfaceFragment(fragment: Fragment, tag: String?, fragmentId: Int) {
        supportFragmentManager.beginTransaction().add(fragmentId, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    protected fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }

    override fun onBackPressed() {
        finish()
    }

    fun hideKeyBoard(view: View) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}