package com.yplay.yspending.data.source.local.suggest

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yplay.yspending.data.model.Suggestion
import com.yplay.yspending.enum.Constant

@Database(entities = [Suggestion::class], version = Constant.SUGGESTION_DB_VERSION, exportSchema = false)
abstract class SuggestionDatabase: RoomDatabase() {
    abstract fun suggestionDao(): SuggestionDao
}