package com.yplay.yspending.data.source.local

import androidx.lifecycle.LiveData
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.data.source.local.spend.SpendDao
import javax.inject.Inject

class SpendRepository @Inject constructor(private val spendDao: SpendDao) {

    suspend fun insert(spend: Spend) {
        spendDao.insert(spend)
    }

    suspend fun update(spend: Spend) {
        spendDao.updateSpend(spend)
    }

    fun getSpendingById(id: Int): Spend? {
        return spendDao.getSpendingById(id)
    }

    fun getAllSpendingGroupByMonthYearLiveData(month: Int, year: Int): LiveData<List<Spend>> {
        return spendDao.getAllSpendingGroupByMonthLiveData(month, year)
    }

    fun getAllSpendingGroupByMonthYear(month: Int, year: Int): List<Spend> {
        return spendDao.getAllSpendingGroupByMonth(month, year)
    }

    fun getAllSpendingByMonthAndYearLiveDay(month: Int, day: Int): LiveData<List<Spend>> {
        return spendDao.getAllSpendingByMonthAndDay(month, day)
    }

    fun getAllSpendingByMonthDayYear(month: Int, day: Int, year: Int): LiveData<List<Spend>> {
        return spendDao.getAllSpendingByMonthDayAndYear(month, day, year)
    }

    fun removeSpending(spends: List<Spend>) {
        spendDao.removeSpending(spends)
    }
}