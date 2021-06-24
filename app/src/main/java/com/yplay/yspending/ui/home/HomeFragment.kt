package com.yplay.yspending.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yplay.yspending.AppViewModelFactory
import com.yplay.yspending.AutoClearedValue
import com.yplay.yspending.R
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.adapter.SuggestionAdapter
import com.yplay.yspending.broadcast.ReminderReceiver
import com.yplay.yspending.data.model.Suggestion
import com.yplay.yspending.databinding.FragmentHomeBinding
import com.yplay.yspending.ui.baseview.HomeBaseActivity
import com.yplay.yspending.ui.root.RootFragment
import java.util.*
import javax.inject.Inject

class HomeFragment : RootFragment(), SuggestionAdapter.ClickEvent {
    companion object {
        fun newInstance() = HomeFragment()
        val TAG = HomeFragment::class.java.simpleName
    }

    private var mView: FragmentHomeBinding? = null
    private var mBinding: AutoClearedValue<FragmentHomeBinding>? = null
    private var mSuggestAdapter: SuggestionAdapter? = null
    //private var mIsPriceUpdating = false

    @Inject
    lateinit var appViewModelFactory: AppViewModelFactory
    private lateinit var mViewModel: HomeViewModel
    private var suggests = mutableListOf<Suggestion>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(LayoutInflater.from(context),
            R.layout.fragment_home, null, false, null)
        mBinding = AutoClearedValue(this)
        mView = binding

        SpendingApp.instance.applicationComponent.plus(HomeModule()).inject(this)
        mViewModel = ViewModelProviders.of(this, appViewModelFactory).get(HomeViewModel::class.java)

        mSuggestAdapter = SuggestionAdapter()
        mSuggestAdapter?.setListener(this)
        binding.rvSuggest.adapter = mSuggestAdapter
        binding.rvSuggest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        dataObservingAndViewHandling()
        numberPickerSetUp()
        mViewModel.getSuggests().observe(viewLifecycleOwner, Observer { suggests ->
            this.suggests.clear()
            this.suggests.addAll(suggests)
            mSuggestAdapter?.setData(this.suggests.toList())
        })
        mViewModel.uiSetup()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    private fun dataObservingAndViewHandling() {
        mViewModel.mUIState.observe(viewLifecycleOwner, Observer { data ->
            data?.currentDateMonth?.let {
                val day = it.get(Calendar.DAY_OF_MONTH)
                val month = (it.get(Calendar.MONTH) + 1)

                if (day < 10) mView?.tvDay?.text = "/0${it.get(Calendar.DAY_OF_MONTH)}"
                else mView?.tvDay?.text = "/${it.get(Calendar.DAY_OF_MONTH)}"

                if (month < 10) mView?.tvMonth?.text = "0${(it.get(Calendar.MONTH) + 1).toString()}"
                else mView?.tvMonth?.text = (it.get(Calendar.MONTH) + 1).toString()
            }
            data?.reminderHour?.let {
                mView?.npHour?.value = it
            }
            data?.reminderMinute?.let {
                mView?.npMinute?.value = it
            }
            data?.reminderState?.let {
                mView?.swReminder?.isChecked = it
            }
            data?.isLoading?.let { loading ->
                if (loading) mView?.pgLoading?.visibility = View.VISIBLE
                else mView?.pgLoading?.visibility = View.GONE
            }
            data?.exceptionMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })
        mView?.btSave?.setOnClickListener {
            if (mView?.actvSpending?.text?.isNotBlank()!! && mView?.actvPrice?.text?.isNotBlank()!!) {
                val spending = mView?.actvSpending?.text?.toString()!!
                val price = mView?.actvPrice?.text?.toString()!!
                mViewModel.saveSpending(spending, price)
                mView?.actvSpending?.setText("")
                mView?.actvPrice?.setText("")
                Toast.makeText(context, context?.getString(R.string.saved), Toast.LENGTH_LONG).show()
                activity?.let { activity ->
                    if (activity is HomeBaseActivity) {
                        activity.hideKeyBoard(it)
                    }
                }
            }
        }
        mView?.swReminder?.setOnCheckedChangeListener { _, isChecked ->
            mViewModel.saveReminderState(isChecked)
            if (isChecked && isReminderChanged()) {
                val calendarSet = Calendar.getInstance()
                calendarSet.set(Calendar.HOUR_OF_DAY, mView?.npHour?.value!!)
                calendarSet.set(Calendar.MINUTE, mView?.npMinute?.value!!)
                Log.d(
                    TAG, "set Reminder hour: ${calendarSet.get(Calendar.HOUR_OF_DAY)}, " +
                        "minute: ${calendarSet.get(Calendar.MINUTE)}")
                setRemind(calendarSet.timeInMillis)
                mViewModel.saveReminderTime(mView?.npHour?.value!!, mView?.npMinute?.value!!)
            }
        }
        mView?.npHour?.setOnValueChangedListener { _, _, newVal ->
            mView?.swReminder?.isChecked = newVal == mViewModel.getCurrentHourRemind()
        }
        mView?.npMinute?.setOnValueChangedListener { _, _, newVal ->
            mView?.swReminder?.isChecked = newVal == mViewModel.getCurrentMinuteRemind()
        }
    }

    private fun isReminderChanged() : Boolean {
        return mView?.npHour?.value!! != mViewModel.getCurrentHourRemind() ||
                mView?.npMinute?.value!! != mViewModel.getCurrentMinuteRemind()
    }

    private fun setRemind(millisecondTime: Long) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC, millisecondTime, AlarmManager.INTERVAL_DAY, pi)
    }

    private fun numberPickerSetUp() {
        mView?.npHour?.minValue = 0
        mView?.npHour?.maxValue = 23

        mView?.npMinute?.minValue = 0
        mView?.npMinute?.maxValue = 59
    }

    override fun onClick(suggest: Suggestion) {
        if (mView?.actvSpending?.hasFocus()!!) mView!!.actvSpending.setText(suggest.suggest)
    }

    override fun getTAG(): String {
        return TAG
    }

}