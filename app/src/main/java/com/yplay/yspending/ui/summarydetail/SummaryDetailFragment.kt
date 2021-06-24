package com.yplay.yspending.ui.summarydetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.yplay.yspending.AppViewModelFactory
import com.yplay.yspending.R
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.adapter.DayDetailAdapter
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.databinding.FragmentSummaryDetailBinding
import com.yplay.yspending.ui.root.RootFragment
import com.yplay.yspending.ui.view.AlertDialogFragment
import com.yplay.yspending.utils.DialogUtils
import javax.inject.Inject

class SummaryDetailFragment : RootFragment(), DayDetailAdapter.ItemSelected, AlertDialogFragment.OnDialogFragmentResult {

    companion object {
        fun newInstance(month: Int, day: Int, year: Int) : SummaryDetailFragment {
            val bundle = Bundle()
            val fragment = SummaryDetailFragment()
            bundle.putInt(SummaryDetailActivity.KEY_DAY, day)
            bundle.putInt(SummaryDetailActivity.KEY_MONTH, month)
            bundle.putInt(SummaryDetailActivity.KEY_YEAR, year)
            fragment.arguments = bundle
            return fragment
        }

        val TAG = SummaryDetailFragment::class.java.simpleName
    }

    private var mAdapter = DayDetailAdapter()
    private var mDay: Int? = null
    private var mMonth: Int? = null
    private var mYear: Int? = null
    private var mDetails = mutableListOf<Spend>()
    private var mView: FragmentSummaryDetailBinding? = null
    private var mSelectedSpends = mutableListOf<Spend>()
    private val mBlankSpend = Spend("", 0, 1, 1, 1)

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private lateinit var mViewModel: SummaryDetailViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        val binding = DataBindingUtil.inflate<FragmentSummaryDetailBinding>(LayoutInflater.from(context),
        R.layout.fragment_summary_detail, null, false, null)

        mView = binding

        SpendingApp.instance.applicationComponent.plus(SummaryDetailModule()).inject(this)
        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(SummaryDetailViewModel::class.java)

        mDetails.add(mBlankSpend)
        mAdapter.setListener(this)
        binding.rvDayDetail.adapter = mAdapter
        binding.rvDayDetail.layoutManager = GridLayoutManager(context, 2)
        observeData()

        arguments?.let {
            mDay = it.getInt(SummaryDetailActivity.KEY_DAY)
            mMonth = it.getInt(SummaryDetailActivity.KEY_MONTH)
            mYear = it.getInt(SummaryDetailActivity.KEY_YEAR)
            if (mDay != null && mMonth != null && mYear != null) {
                binding.tvTitle.text = "$mDay - $mMonth - $mYear"
                mViewModel.getDetailByDayMonthYear(mMonth!!, mDay!!, mYear!!).observe(viewLifecycleOwner, Observer { details ->
                    Log.d(TAG, "View model observing $details")
                    mDetails.clear()
                    mDetails.add(mBlankSpend)
                    mDetails.addAll(details)
                    mAdapter.setData(mDetails)
                    showLoading(false)
                })
            }
        }

        binding.btPrev.setOnClickListener { activity?.finish() }
        binding.btClose.setOnClickListener {
            switchMode(false)
            mAdapter.switchToNormal()
        }
        binding.btAll.setOnClickListener {
            mAdapter.selectAll()
        }
        binding.btRemove.setOnClickListener {
            val description = getString(R.string.confirm_delete)
            activity?.let {
                DialogUtils.openConfirmDialog(childFragmentManager, description)
            }

        }

        return binding.root
    }

    private fun switchMode(isSelect: Boolean) {
        if (isSelect) {
            mView?.btPrev?.visibility = View.INVISIBLE
            mView?.llSelectOptionLeft?.visibility = View.VISIBLE
            mView?.llSelectOptionRight?.visibility = View.VISIBLE
        } else {
            mView?.btPrev?.visibility = View.VISIBLE
            mView?.llSelectOptionLeft?.visibility = View.GONE
            mView?.llSelectOptionRight?.visibility = View.GONE
        }
    }

    private fun observeData() {
        mViewModel.mUIState.observe(viewLifecycleOwner, Observer {
            it?.isLoading?.let { show ->
                showLoading(show)
            }
        })
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            mView?.pgLoading?.visibility = View.VISIBLE
        } else {
            mView?.pgLoading?.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun getTAG(): String {
        return TAG
    }

    override fun onSelected(spends: List<Spend>?) {
        mSelectedSpends.clear()
        val filtered = spends?.filter { it != mBlankSpend }
        Log.d(TAG, "onSelected $filtered")
        filtered?.let { mSelectedSpends.addAll(it) }
        mView?.tvCounter?.text = mSelectedSpends.size.toString()
    }

    override fun onSelectMode() {
        switchMode(true)
    }

    override fun onDialogResult(tag: String, actionId: Int, data: Intent) {
        if (tag == DialogUtils.DELETE_CONFIRM) {
            if (actionId == R.id.tv_ok) {
                mViewModel.removeSpend(mSelectedSpends)
                mAdapter.switchToNormal()
                switchMode(false)
            }
        }
    }

    override fun onItemSelected(spend: Spend?) {
        var spending = ""
        var price = ""
        var id = -1
        if (spend != null) {
            id = spend.id
            spending = spend.thingForPay
            price = spend.price.toString()
        }
        activity?.let {
            (it as SummaryDetailActivity).openEditFragment(id, spending, price, mDay!!, mMonth!!, mYear!!)
        }
    }

}