package com.yplay.yspending.data.source.local.spend

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yplay.yspending.data.model.Spend

@Dao
abstract class SpendDao {

    @Query("SELECT * FROM spend")
    abstract fun getAllSpending(): List<Spend>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(spend: Spend)

    @Query("SELECT * FROM spend WHERE month = :month AND year = :year")
    abstract fun getAllSpendingGroupByMonthLiveData(month: Int, year: Int): LiveData<List<Spend>>

    @Query("SELECT * FROM spend WHERE month = :month AND year = :year")
    abstract fun getAllSpendingGroupByMonth(month: Int, year: Int): List<Spend>

    @Query("SELECT * FROM spend WHERE month = :month AND day = :day")
    abstract fun getAllSpendingByMonthAndDay(month: Int, day: Int): LiveData<List<Spend>>

    @Query("SELECT * FROM spend WHERE month = :month AND day = :day AND year = :year")
    abstract fun getAllSpendingByMonthDayAndYear(month: Int, day: Int, year: Int): LiveData<List<Spend>>

    @Update
    abstract fun updateSpend(spend: Spend)

    @Query("SELECT * FROM spend WHERE id=:id")
    abstract fun getSpendingById(id: Int): Spend?

    @Delete
    abstract fun removeSpending(spends: List<Spend>)
}