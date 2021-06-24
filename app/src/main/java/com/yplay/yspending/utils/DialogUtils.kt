package com.yplay.yspending.utils

import androidx.fragment.app.FragmentManager
import com.yplay.yspending.R
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.ui.view.AlertDialogFragment

object DialogUtils {

    const val EDIT_SPENDING = "EDIT_SPENDING"
    const val DELETE_CONFIRM = "DELETE_CONFIRM"

    fun openEditDialog(activity: FragmentManager, spending: String = "", price: String = "") {
        AlertDialogFragment.Builder(R.layout.dialog_add_or_edit_spending)
            .addDismissView(R.id.tv_cancel)
            .addDismissView(R.id.tv_ok)
            .addTextView(R.id.tv_description, SpendingApp.instance.getString(R.string.dialog_detail))
            .addEditTextView(R.id.et_spending, spending)
            .addEditTextView(R.id.et_price, price)
            .show(activity, EDIT_SPENDING)
    }

    fun openConfirmDialog(activity: FragmentManager, description: String) {
        AlertDialogFragment.Builder(R.layout.dialog_confirm)
            .addDismissView(R.id.tv_cancel)
            .addDismissView(R.id.tv_ok)
            .addTextView(R.id.tv_description, description)
            .show(activity, DELETE_CONFIRM)
    }

}