package com.yplay.yspending.ui.summarydetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.yplay.yspending.R
import com.yplay.yspending.ui.root.RootActivity

class SummaryDetailActivity : RootActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        Log.d(TAG, "onCreate")
        var fragment = supportFragmentManager.findFragmentById(R.id.content) as SummaryDetailFragment?

        intent?.let {
            val day = it.extras?.getInt(KEY_DAY)
            val month = it.extras?.getInt(KEY_MONTH)
            val year = it.extras?.getInt(KEY_YEAR)

            Log.d(TAG, "onCreate day $day month $month year $year")
            if (fragment == null) {
                if (day != null && month != null && year != null) {
                    fragment = SummaryDetailFragment.newInstance(month, day, year)
                    addFragment(fragment!!, R.id.content)
                }
            }
        }
    }

    fun openEditFragment(id: Int, thing: String, price: String, day: Int, month: Int, year: Int) {
        var fragment = supportFragmentManager.findFragmentById(R.id.content)
        if (fragment != null) {
            fragment = SummaryDetailDialogFragment.newInstance(id, thing, price, day, month, year)
            addSurfaceFragment(fragment, R.id.content, SummaryDetailDialogFragment.TAG)
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        Log.d(TAG, "onCreateView")
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onStop")
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.content)
        if (currentFragment is SummaryDetailDialogFragment) {
            removeFragment(currentFragment)
        } else {
            finish()
        }
    }

    companion object {
        const val KEY_DAY = "KEY_DAY"
        const val KEY_MONTH = "KEY_MONTH"
        const val KEY_YEAR = "KEY_YEAR"
        private val TAG = SummaryDetailActivity::class.simpleName

        @JvmStatic
        fun startActivity(activity: Activity, month: Int, day: Int, year: Int) {
            val intent = Intent(activity, SummaryDetailActivity::class.java)
            intent.putExtra(KEY_DAY, day)
            intent.putExtra(KEY_MONTH, month)
            intent.putExtra(KEY_YEAR, year)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(intent)
        }
    }

}