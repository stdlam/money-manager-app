package com.yplay.yspending.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.data.model.Suggestion
import com.yplay.yspending.data.source.local.SpendRepository
import com.yplay.yspending.data.source.local.SuggestionRepository
import com.yplay.yspending.manager.SharePreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val mSpendRepository: SpendRepository,
                                        private val mSuggestionRepository: SuggestionRepository) : ViewModel() {

    private var mUIData: MutableLiveData<UIDataWrapper> = MutableLiveData()
    val mUIState: LiveData<UIDataWrapper> get() = mUIData

    fun uiSetup() {
        val date = Calendar.getInstance()
        var reminderHour = SharePreferenceManager.getHourReminder()
        var reminderMinute = SharePreferenceManager.getMinuteReminder()
        val reminderState = SharePreferenceManager.getStateReminder()
        if (reminderHour == -1 || reminderMinute == -1) {
            reminderHour = date.get(Calendar.HOUR_OF_DAY)
            reminderMinute = date.get(Calendar.MINUTE)
        }
        emitData(currentDateMonth = date, reminderHour = reminderHour, reminderMinute = reminderMinute, reminderState = reminderState)
    }

    fun saveReminderState(state: Boolean) {
        SharePreferenceManager.setReminderState(state)
    }

    fun saveReminderTime(hour: Int, minute: Int) {
        SharePreferenceManager.setReminder(hour, minute)
    }

    fun getCurrentHourRemind() = SharePreferenceManager.getHourReminder()

    fun getCurrentMinuteRemind() = SharePreferenceManager.getMinuteReminder()

    fun saveSpending(spending: String, price: String) {
        emitData(isLoading = true)
        CoroutineScope(Dispatchers.Main).launch {
            val date = Calendar.getInstance()
            try {
                val longPrice = price.toLong()
                val spend = Spend(spending, longPrice, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH), date.get(Calendar.YEAR))
                withContext(Dispatchers.IO) {
                    val suggestions = mSuggestionRepository.getRawSuggests()
                    val oldSuggestion = suggestions.find {
                        it.suggest == spending
                    }
                    if (oldSuggestion != null) {
                        var oldUsed = oldSuggestion.used
                        oldUsed++
                        mSuggestionRepository.update(oldSuggestion.id, oldUsed)
                    }
                    else {
                        val suggest = Suggestion(spending, 0)
                        mSuggestionRepository.insert(suggest)
                    }
                    mSpendRepository.insert(spend)
                    emitData(isLoading = false)
                }
            } catch (e: Exception) {
                emitData(isLoading = false, exceptionMessage = e.message)
            }
        }

    }

    fun getSuggests() = mSuggestionRepository.getSuggestByLiveData()

    private fun emitData(isLoading: Boolean? = null, currentDateMonth: Calendar? = null,
                         reminderHour: Int? = null, reminderMinute: Int? = null, reminderState: Boolean? = null, exceptionMessage: String? = null) {
        val data = UIDataWrapper(isLoading, currentDateMonth, reminderHour, reminderMinute, reminderState, exceptionMessage)
        mUIData.postValue(data)
    }

    data class UIDataWrapper(var isLoading: Boolean?, var currentDateMonth: Calendar?,
                             var reminderHour: Int?, var reminderMinute: Int?, var reminderState: Boolean?, var exceptionMessage: String?)
}