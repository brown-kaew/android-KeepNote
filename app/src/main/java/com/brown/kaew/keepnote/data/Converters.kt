package com.brown.kaew.keepnote.data

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun dateStampToCalender(value: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = value
        return calendar
    }

//    For short
//    fun dateStampToCalender(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }
}