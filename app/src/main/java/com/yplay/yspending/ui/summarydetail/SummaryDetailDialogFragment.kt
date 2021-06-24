package com.yplay.yspending.ui.summarydetail

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.yplay.yspending.AppViewModelFactory
import com.yplay.yspending.R
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.databinding.DialogAddOrEditSpendingBinding
import javax.inject.Inject

class SummaryDetailDialogFragment : DialogFragment() {

    companion object {
        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_THING = "EXTRA_THING"
        private const val EXTRA_PRICE = "EXTRA_PRICE"
        private const val EXTRA_DAY = "EXTRA_DAY"
        private const val EXTRA_MONTH = "EXTRA_MONTH"
        private const val EXTRA_YEAR = "EXTRA_YEAR"
        const val TAG = "SummaryDetailDialogFragment"
        fun newInstance(id: Int, thing: String, price: String, day: Int, month: Int, year: Int) : SummaryDetailDialogFragment {
            val bundle = Bundle()
            val fragment = SummaryDetailDialogFragment()
            bundle.putInt(EXTRA_ID, id)
            bundle.putString(EXTRA_THING, thing)
            bundle.putString(EXTRA_PRICE, price)
            bundle.putInt(EXTRA_DAY, day)
            bundle.putInt(EXTRA_MONTH, month)
            bundle.putInt(EXTRA_YEAR, year)
            fragment.arguments = bundle
            return fragment
        }
    }
    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private lateinit var mViewModel: SummaryDetailViewModel

    private var mView: DialogAddOrEditSpendingBinding? = null
    private var mId: Int? = null
    private var mDay: Int? = null
    private var mMonth: Int? = null
    private var mYear: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = DataBindingUtil.inflate<DialogAddOrEditSpendingBinding>(LayoutInflater.from(context),
            R.layout.dialog_add_or_edit_spending, null, false, null)
        mView = view
        SpendingApp.instance.applicationComponent.plus(SummaryDetailModule()).inject(this)
        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(SummaryDetailViewModel::class.java)

        arguments?.let {
            mId = it.getInt(EXTRA_ID)
            view.etSpending.setText(it.getString(EXTRA_THING))
            view.etPrice.setText(it.getString(EXTRA_PRICE))
            mDay = it.getInt(EXTRA_DAY)
            mMonth = it.getInt(EXTRA_MONTH)
            mYear = it.getInt(EXTRA_YEAR)
        }

        view.tvOk.setOnClickListener {
            mId.let { id ->
                mViewModel.saveDetail(id!!, view.etSpending.text.toString(), view.etPrice.text.toString(), mDay!!, mMonth!!, mYear!!)
            }
            finish(it)
        }

        view.tvCancel.setOnClickListener { finish(it) }

        return view.root
    }

    private fun finish(view: View) {
        hideKeyBoard(view)
        super.dismiss()
    }

    private fun hideKeyBoard(view: View) {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}