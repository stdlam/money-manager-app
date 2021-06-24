package com.yplay.yspending.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yplay.yspending.AppViewModelFactory
import com.yplay.yspending.AutoClearedValue
import com.yplay.yspending.R
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.adapter.SpendingAdapter
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.databinding.FragmentOverviewBinding
import com.yplay.yspending.manager.ConvertingManager
import com.yplay.yspending.ui.root.RootFragment
import com.yplay.yspending.ui.summarydetail.SummaryDetailActivity
import java.util.*
import javax.inject.Inject

class OverviewFragment : RootFragment(), SpendingAdapter.ClickEvent {
    companion object {
        fun newInstance() = OverviewFragment()
        val TAG = OverviewFragment::class.java.simpleName
    }

    private var mView: FragmentOverviewBinding? = null
    private var mBinding: AutoClearedValue<FragmentOverviewBinding>? = null
    private var mSpendAdapter: SpendingAdapter? = null
    private var currentMonth: Int? = null
    private var currentYear: Int? = null
    private var currentDay: Int? = null
    private var mSpendingListGroupByDate = mutableListOf<Spend>()
    private var mCalendar = Calendar.getInstance()

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private lateinit var mViewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        val binding = DataBindingUtil.inflate<FragmentOverviewBinding>(LayoutInflater.from(context),
        R.layout.fragment_overview, null, false, null)
        mBinding = AutoClearedValue(this)
        mView = binding
        mSpendAdapter = SpendingAdapter()
        mSpendAdapter?.setListener(this)
        binding.rvMonth.adapter = mSpendAdapter
        binding.rvMonth.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        SpendingApp.instance.applicationComponent.plus(OverviewModule()).inject(this)
        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(OverviewViewModel::class.java)
        dataObservingAndViewHandling()
        mViewModel.uiSetup()

        binding.vSummaryMonth.isSelected = true

        return binding.root
    }

    private fun dataObservingAndViewHandling() {
        mViewModel.mUIState.observe(viewLifecycleOwner, androidx.lifecycle.Observer { data ->
            data?.isLoading?.let {
                if (it) {
                    mView?.pgLoading?.visibility = View.VISIBLE
                }
                else {
                    mView?.pgLoading?.visibility = View.GONE
                }
            }
            data?.day?.let { currentDay = it }
            data?.month?.let {
                currentMonth = it
                if (currentMonth!! < 9) {
                    mView?.tvDate?.text = "0${(currentMonth!! + 1)}"
                }
                else {
                    mView?.tvDate?.text = "${(currentMonth!! + 1)}"
                }

            }
            data?.year?.let {
                currentYear = it
                mView?.tvDate?.text = "${mView?.tvDate?.text}/${currentYear.toString()}"
            }
            data?.spendingList?.let { spends ->
                mSpendingListGroupByDate.clear()
                mSpendingListGroupByDate.addAll(spends)
                currentMonth?.let { month ->
                    mSpendAdapter?.setData(mSpendingListGroupByDate.reversed(), month)
                }
            }
            data?.spendTotal?.let { total ->
                val price = ConvertingManager.priceFormat(total.toDouble())
                mView?.vSummaryMonth?.setText(String.format(getString(R.string.total), price))
            }
        })

        mView?.btNext?.setOnClickListener {
            if (currentMonth != null && currentDay != null && currentYear != null)  {
                currentMonth = currentMonth!! + 1
                if (currentMonth!! > 11) {
                    currentYear = currentYear!! + 1
                    currentMonth = 0
                }
                mViewModel.updateNewest(currentDay!!, currentMonth!!, currentYear!!)
            }
        }

        mView?.btPrev?.setOnClickListener {
            if (currentMonth != null && currentDay != null && currentYear != null) {
                currentMonth = currentMonth!! - 1
                if (currentMonth!! < 0) {
                    currentYear = currentYear!! - 1
                    currentMonth = 11
                }
                mViewModel.updateNewest(currentDay!!, currentMonth!!, currentYear!!)
            }
        }

        mView?.swpRefresh?.setOnRefreshListener {
            if (currentYear != null && currentMonth != null && currentDay != null) {
                mViewModel.updateNewest(currentDay!!, currentMonth!!, currentYear!!)
            }
            mView?.swpRefresh?.isRefreshing = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (currentMonth != null && currentYear != null && currentDay != null) {
            mViewModel.getAllSpendingByMonthYear(currentMonth!!, currentYear!!).observe(viewLifecycleOwner, Observer {
                mView?.pgLoading?.visibility = View.GONE
                mViewModel.handleListSpend(it, currentDay!!, currentMonth!!, currentYear!!, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            })
        }
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
        Log.d(TAG, "onDestroy")
    }

    override fun getTAG(): String {
        return TAG
    }

    fun onBackPressed() {
        val isSelecting = mSpendAdapter?.isSelectMode()!!
        if (isSelecting) {
            mSpendAdapter?.returnNormalMode()
        }
    }

    override fun onClick(month: Int, day: Int, year: Int) {
        activity?.let {
            SummaryDetailActivity.startActivity(it, month, day, year)
        }
    }

}