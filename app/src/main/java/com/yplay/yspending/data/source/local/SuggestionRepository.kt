package com.yplay.yspending.data.source.local

import androidx.lifecycle.LiveData
import com.yplay.yspending.data.model.Suggestion
import com.yplay.yspending.data.source.local.suggest.SuggestionDao
import javax.inject.Inject

class SuggestionRepository @Inject constructor(private val suggestionDao: SuggestionDao) {
    fun getSuggestByLiveData() : LiveData<List<Suggestion>> {
        return suggestionDao.getAllSuggests()
    }

    suspend fun getRawSuggests(): List<Suggestion> {
        return suggestionDao.getAllRawSuggests()
    }

    suspend fun insert(suggestion: Suggestion) {
        suggestionDao.insert(suggestion)
    }

    suspend fun update(id: Int, number: Int) {
        suggestionDao.updateUsed(id, number)
    }
}