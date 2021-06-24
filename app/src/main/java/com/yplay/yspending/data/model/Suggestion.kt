package com.yplay.yspending.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "suggestion")
data class Suggestion constructor(var suggest: String, var used: Int) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}