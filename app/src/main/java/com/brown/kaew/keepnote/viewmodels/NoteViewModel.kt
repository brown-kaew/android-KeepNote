package com.brown.kaew.keepnote.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.data.NoteDatabase
import com.brown.kaew.keepnote.data.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

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