package com.yplay.yspending.ui.splashscreen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.yplay.yspending.AutoClearedValue
import com.yplay.yspending.R
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.databinding.FragmentSplashScreenBinding
import com.yplay.yspending.ui.baseview.HomeBaseActivity
import com.yplay.yspending.ui.root.RootFragment
import com.yplay.yspending.ui.summarydetail.SummaryDetailActivity

class SplashFragment : RootFragment() {
    companion object {
        fun newInstance() = SplashFragment()
        val TAG = SplashFragment::class.java.simpleName
        private const val DELAY_DURATION = 1000L
    }

    private val mHandler = Handler()
    private val mSplashHandler = Runnable {
        goToNext()
    }
    private var mBinding: AutoClearedValue<FragmentSplashScreenBinding>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSplashScreenBinding>(LayoutInflater.from(context),
            R.layout.fragment_splash_screen, null, false, null)
        mBinding = AutoClearedValue(this)
        SpendingApp.instance.applicationComponent.plus(SplashModule()).inject(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(mSplashHandler, DELAY_DURATION)
    }

    private fun goToNext() {
        activity?.let {
            HomeBaseActivity.start(requireContext())
            it.finish()
        }
    }
}