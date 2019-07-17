package com.brown.kaew.keepnote.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class NoteRepository private constructor(private val noteDao: NoteDao) {

    companion object {
        // For Singleton instantiation
        private var instances: NoteRepository? = null

        fun getInstance(noteDao: NoteDao): NoteRepository =
            instances ?: NoteRepository(noteDao).also { instances = it }

    }

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAll()

    fun getById(id: Long): LiveData<Note> {
        return noteDao.getById(id)
    }

    suspend fun getById2(id: Long): Note {
        return withContext(Dispatchers.IO) {
            noteDao.getById2(id)
        }
    }

    suspend fun insertNote(note: Note): Long {
        return withContext(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    suspend fun insertNotes(vararg note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.insertAll(*note)
        }
    }

    suspend fun insertListOfNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            noteDao.insertList(notes)
        }
    }

    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    suspend fun deleteAllAndInsert(list: List<Note>) {
        withContext(Dispatchers.IO) {
            noteDao.deleteAllAndInsert(list)
        }
    }

}