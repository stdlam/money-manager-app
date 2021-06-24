package com.yplay.yspending.adapter

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.yplay.yspending.ui.root.RootFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, listFragment: ArrayList<Fragment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mListFragments = listFragment
    private var mFragmentManager: FragmentManager = fragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val frameLayout =  FrameLayout(parent.context)
        frameLayout.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return object : RecyclerView.ViewHolder(frameLayout) {
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val fragment = mFragmentManager.findFragmentById(holder.itemView.id)
        if (fragment != null) {
            mFragmentManager.beginTransaction().remove(fragment).commitNowAllowingStateLoss()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            holder.itemView.id = View.generateViewId()
        }
        else {
            holder.itemView.id = System.currentTimeMillis().toInt()
        }
        holder.itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                holder.itemView.removeOnAttachStateChangeListener(this)
                val baseFragment: RootFragment = mListFragments[holder.adapterPosition] as RootFragment
                val currentFragment = mFragmentManager.findFragmentByTag(baseFragment.getTAG())
                if (currentFragment != null) {
                    mFragmentManager.beginTransaction().remove(currentFragment).commitNowAllowingStateLoss()
                }
                mFragmentManager.beginTransaction().add(v!!.id, baseFragment, baseFragment.getTAG()).commitNowAllowingStateLoss()
            }

            override fun onViewDetachedFromWindow(v: View?) {

            }
        })
    }

    override fun getItemCount(): Int {
        return mListFragments.size
    }

}