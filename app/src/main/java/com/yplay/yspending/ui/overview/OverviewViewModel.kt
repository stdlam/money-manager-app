package com.yplay.yspending.ui.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.data.source.local.SpendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class OverviewViewModel @Inject constructor(private val mSpendingRepository: SpendRepository): ViewModel() {

    companion object {
        private val TAG = OverviewViewModel::class.simpleName
    }

    private var mUIData: MutableLiveData<UIDataWrapper> = MutableLiveData()
    val mUIState: LiveData<UIDataWrapper> get() = mUIData

    fun uiSetup() {
        emitData(isLoading = true)
        val date = Calendar.getInstance()
        val day = date.get(Calendar.DAY_OF_MONTH)
        val month = date.get(Calendar.MONTH)
        val year = date.get(Calendar.YEAR)
        val monthLength = date.getActualMaximum(Calendar.DAY_OF_MONTH)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "uiSetup days=$monthLength")
                val spendingList = mSpendingRepository.getAllSpendingGroupByMonthYear(month, year)
                handleListSpend(spendingList, day, month, year, monthLength)
            }
        }
    }

    fun handleListSpend(spendingList: List<Spend>, day: Int, month: Int, year: Int, monthLength: Int) {
        spendingList.sortedBy { it.day }
        val filtered = mutableListOf<Spend>()
        for (d in 1 until monthLength + 1) {
            val spendByDay = spendingList.filter { it.day == d }
            if (spendByDay.isEmpty()) {
                val blankSpend = Spend("", 0, d, month, year)
                filtered.add(blankSpend)
            } else {
                filtered.addAll(spendByDay)
            }
        }

        val total = spendingList.sumByDouble { it.price.toDouble() }
        emitData(isLoading = false, day = day, month = month, year = year, spendingList = filtered, spendTotal = total.toLong())
    }

    fun getAllSpendingByMonthYear(month: Int, year: Int): LiveData<List<Spend>> {
        emitData(isLoading = true)
        return mSpendingRepository.getAllSpendingGroupByMonthYearLiveData(month, year)
    }

    fun updateNewest(day: Int, month: Int, year: Int) {
        emitData(isLoading = true)
        val date = Calendar.getInstance()
        val monthLength = date.getActualMaximum(Calendar.DAY_OF_MONTH)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val spendingList = mSpendingRepository.getAllSpendingGroupByMonthYear(month, year)
                handleListSpend(spendingList, day, month, year, monthLength)
            }
        }
    }

    private fun emitData(isLoading: Boolean? = null, day: Int? = null, month: Int? = null, year: Int? = null, spendingList: List<Spend>? = null, spendTotal: Long? = null) {
        val data = UIDataWrapper(isLoading, day, month, year, spendingList, spendTotal)
        mUIData.postValue(data)
    }

    data class UIDataWrapper(var isLoading: Boolean?, var day: Int?, var month: Int?, var year: Int?, var spendingList: List<Spend>?, var spendTotal: Long?)
}