package com.yplay.yspending.ui.baseview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yplay.yspending.R
import com.yplay.yspending.ui.root.RootActivity

class HomeBaseActivity : RootActivity() {

    private var mFragmentHome: HomeBaseFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreateView")
        setContentView(R.layout.activity_base)
        mFragmentHome = supportFragmentManager.findFragmentById(R.id.content) as HomeBaseFragment?
        if (mFragmentHome == null) {
            mFragmentHome = HomeBaseFragment.newInstance()
            addFragment(mFragmentHome!!, R.id.content)
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
        mFragmentHome?.onActivityBackPressed()
    }

    companion object {
        private val TAG = HomeBaseActivity::class.simpleName
        fun start(context: Context) {
            context.startActivity(Intent(context, HomeBaseActivity::class.java))
        }
    }

}