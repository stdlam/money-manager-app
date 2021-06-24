package com.yplay.yspending.data.source.local.suggest

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yplay.yspending.data.model.Suggestion

@Dao
abstract class SuggestionDao {
    @Query("SELECT * FROM suggestion ORDER BY used DESC")
    abstract fun getAllSuggests(): LiveData<List<Suggestion>>

    @Query("SELECT * FROM suggestion ORDER BY used DESC")
    abstract fun getAllRawSuggests(): List<Suggestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(suggestion: Suggestion)

    @Query("UPDATE suggestion SET used = :number WHERE id = :id")
    abstract fun updateUsed(id: Int, number: Int)
}