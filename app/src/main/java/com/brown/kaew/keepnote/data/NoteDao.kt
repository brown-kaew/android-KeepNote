package com.brown.kaew.keepnote.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Transaction
    fun deleteAllAndInsert(list: List<Note>) {
        deletedAll()
        insertList(list)
    }

    @Query("SELECT * FROM note ORDER BY nId DESC")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE nId = :id")
    fun getById(id: Long): LiveData<Note>

    @Query("SELECT * FROM note WHERE nId = :id")
    fun getById2(id: Long): Note

    @Query("DELETE FROM note")
    fun deletedAll()

    @Insert
    fun insert(note: Note): Long

    @Insert
    fun insertAll(vararg notes: Note)

    @Insert
    fun insertList(note: List<Note>)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun delete(note: Note)

}