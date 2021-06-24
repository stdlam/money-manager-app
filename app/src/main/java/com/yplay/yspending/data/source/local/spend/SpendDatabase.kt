package com.yplay.yspending.data.source.local.spend

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.enum.Constant

@Database(entities = [Spend::class], version = Constant.SPEND_DB_VERSION, exportSchema = false)
abstract class SpendDatabase: RoomDatabase() {
    abstract fun spendDao(): SpendDao
}