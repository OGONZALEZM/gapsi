package com.ogonzalezm.testgepsi.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recent_search",
    indices = [Index(value = ["query"], unique = true)]
)
data class RecentSearch(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val query: String,
)