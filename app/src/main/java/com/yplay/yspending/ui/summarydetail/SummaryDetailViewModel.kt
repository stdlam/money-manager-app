package com.yplay.yspending.ui.summarydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.data.source.local.SpendRepository
import com.yplay.yspending.data.source.local.SuggestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SummaryDetailViewModel @Inject constructor(private val mSpendRepository: SpendRepository,
                                                 private val mSuggestionRepository: SuggestionRepository) : ViewModel() {

    private var mUIData: MutableLiveData<UIDataWrapper> = MutableLiveData()
    val mUIState: LiveData<UIDataWrapper> get() = mUIData

    fun getDetailByDayMonthYear(month: Int, day: Int, year: Int): LiveData<List<Spend>> {
        emitData(isLoading = true)
        return mSpendRepository.getAllSpendingByMonthDayYear(month, day, year)
    }

    fun saveDetail(id: Int, thing: String, price: String, day: Int, month: Int, year: Int) {
        emitData(isLoading = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val spend = mSpendRepository.getSpendingById(id)
                if (spend != null) {
                    spend.thingForPay = thing
                    spend.price = toLong(price)
                    mSpendRepository.update(spend)
                } else if (thing.isNotBlank() && price.isNotBlank()) {
                    // insert new item
                    if (checkIfValidPrice(price)) {
                        mSpendRepository.insert(Spend(thing, price.toLong(), day, month, year))
                    }
                }
                emitData(isLoading = false)
            }
        }
    }

    fun removeSpend(spends: List<Spend>) {
        emitData(isLoading = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mSpendRepository.removeSpending(spends)
            }
            emitData(isLoading = false)
        }
    }

    private fun checkIfValidPrice(price: String): Boolean {
        return !price.contains('.')
    }

    private fun toLong(price: String): Long {
        return if (price.isBlank()) 0
        else price.toLong()
    }

    private fun emitData(isLoading: Boolean? = null, spendData: List<Spend>? = null, exceptionMessage: String? = null) {
        val data = UIDataWrapper(isLoading, spendData, exceptionMessage)
        mUIData.postValue(data)
    }

    data class UIDataWrapper(var isLoading: Boolean?, var data: List<Spend>?, var exceptionMessage: String?)

}