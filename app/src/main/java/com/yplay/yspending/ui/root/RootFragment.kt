package com.yplay.yspending.ui.root

import android.provider.ContactsContract
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

open class RootFragment : Fragment() {
    companion object {
        val TAG = RootFragment::class.java.simpleName
    }

    open fun getTAG() = TAG

    open fun onActivityBackPressed() {

    }

}