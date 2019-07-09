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

    suspend fun insertNote(note: Note) {
        withContext(Dispatchers.IO) {
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
}