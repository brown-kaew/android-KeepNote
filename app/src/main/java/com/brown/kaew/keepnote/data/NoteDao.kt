package com.brown.kaew.keepnote.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE nId = :id")
    fun getById(id: Int): LiveData<Note>

    @Insert
    fun insert(note: Note)

    @Insert
    fun insertAll(vararg notes: Note)

    @Insert
    fun insertList(merchant: List<Note>)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun delete(note: Note)

}