package com.brown.kaew.keepnote.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "note")
data class Note(
    @ColumnInfo var title: String = "",
    @ColumnInfo var note: String = "",
    @ColumnInfo var date: Calendar = Calendar.getInstance()
) {
    @PrimaryKey(autoGenerate = true)
    var nId: Long = 0

    var isSelected: Boolean = false
}
