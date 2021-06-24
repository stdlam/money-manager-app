package com.yplay.yspending.ui.baseview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yplay.yspending.AutoClearedValue
import com.yplay.yspending.R
import com.yplay.yspending.adapter.ViewPagerAdapter
import com.yplay.yspending.databinding.FragmentBaseBinding
import com.yplay.yspending.enum.HomeBaseTab
import com.yplay.yspending.ui.home.HomeFragment
import com.yplay.yspending.ui.overview.OverviewFragment
import com.yplay.yspending.ui.root.RootFragment
import androidx.core.view.forEach as forEach

class HomeBaseFragment : RootFragment() {
    companion object {
        fun newInstance() = HomeBaseFragment()
        val TAG = HomeBaseFragment::class.java.simpleName
    }

    private var mCurrentTab = HomeBaseTab.HOME_TAB
    private lateinit var mBinding: AutoClearedValue<FragmentBaseBinding>
    private lateinit var mViewDataBinding: FragmentBaseBinding
    private val mListFragment = arrayListOf<Fragment>()
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mViewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        val binding = DataBindingUtil.inflate<FragmentBaseBinding>(LayoutInflater.from(context), R.layout.fragment_base,
        null, false, null)
        mViewDataBinding = binding
        mBinding = AutoClearedValue(this)

        setUpViewPager(binding.vpHome)
        mViewPager = binding.vpHome
        mBottomNavigationView = binding.btnvHome
        binding.btnvHome.menu.getItem(0).setIcon(R.drawable.ic_home_active)
        binding.btnvHome.menu.forEach {
            val itemView = binding.btnvHome.findViewById<View>(it.itemId)
            itemView.setOnLongClickListener { item ->
                item.isHapticFeedbackEnabled = false
                true
            }
        }
        binding.btnvHome.setOnNavigationItemSelectedListener { item ->
            val menu = binding.btnvHome.menu
            menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home)
            menu.findItem(R.id.navigation_overview).setIcon(R.drawable.ic_overview)
            mCurrentTab = when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.drawable.ic_home_active)
                    HomeBaseTab.HOME_TAB
                }
                R.id.navigation_overview -> {
                    item.setIcon(R.drawable.ic_overview_active)
                    HomeBaseTab.OVERVIEW_TAB
                }
                else -> HomeBaseTab.HOME_TAB
            }
            Log.d(TAG, "on navigation item selected tab $mCurrentTab index 0 ${mListFragment[0]}, index 1 ${mListFragment[1]}")
            binding.vpHome.setCurrentItem(mCurrentTab, false)
            true
        }

        return binding.root
    }

    @Suppress("DEPRECATION")
    private fun setUpViewPager(vp: ViewPager2) {
        var homeFragment = childFragmentManager.findFragmentByTag(HomeFragment.TAG)
        var overviewFragment = childFragmentManager.findFragmentByTag(OverviewFragment.TAG)

        if (homeFragment == null) homeFragment = HomeFragment.newInstance()
        if (overviewFragment == null) overviewFragment = OverviewFragment.newInstance()

        Log.d(TAG, "setUpViewPager homeFragment $homeFragment, overviewFragment $overviewFragment")

        mListFragment.clear()
        mListFragment.add(homeFragment)
        mListFragment.add(overviewFragment)

        val adapter = ViewPagerAdapter(requireFragmentManager(), mListFragment)
        vp.adapter = adapter
        /*if (vp.getChildAt(0) != null) {
            (vp.getChildAt(0) as RecyclerView).setItemViewCacheSize(2)
        }*/
        vp.isUserInputEnabled = false
    }

    override fun onActivityBackPressed() {
        val overviewFragment = childFragmentManager.findFragmentByTag(OverviewFragment.TAG)
        if (mCurrentTab == HomeBaseTab.OVERVIEW_TAB) {
            (overviewFragment as OverviewFragment).onBackPressed()
        } else {
            activity?.finish()
        }
    }

    override fun getTAG(): String {
        return TAG
    }

}