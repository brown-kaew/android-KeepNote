package com.brown.kaew.keepnote

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): LiveData<List<Note>>

    @Insert
    fun insert(note: Note)

    @Insert
    fun insertAll(vararg notes: Note)

    @Insert
    fun insertList(merchant: List<Note>)

    @Delete
    fun delete(note: Note)

}