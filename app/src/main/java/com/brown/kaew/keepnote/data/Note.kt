package com.brown.kaew.keepnote.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    @ColumnInfo val title: String,
    @ColumnInfo val note: String,
    @ColumnInfo val date: String = Date().time.toString()
) {
    @PrimaryKey(autoGenerate = true)
    var nId: Int = 0
}
