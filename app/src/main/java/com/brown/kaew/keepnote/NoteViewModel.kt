package com.brown.kaew.keepnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository by lazy { NoteRepository(NoteDatabase.getInstance(application.applicationContext)) }

    fun getAllNote() = repository.getAllNotes()

    fun insertNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun insertNotes(vararg notes: Note) {
        viewModelScope.launch {
            repository.insertNotes(*notes)
        }
    }

    fun insertListOfNotes(list: List<Note>) {
        viewModelScope.launch {
            repository.insertListOfNotes(list)
        }
    }
}