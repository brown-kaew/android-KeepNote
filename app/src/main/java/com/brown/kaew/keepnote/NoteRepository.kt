package com.brown.kaew.keepnote

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class NoteRepository(private val db: NoteDatabase) {

    private var noteDao: NoteDao = db.noteDao()
    private lateinit var allNotes: LiveData<List<Note>>


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