package com.brown.kaew.keepnote.viewmodels

import android.app.Application
import android.util.Log
import android.view.ActionMode
import androidx.lifecycle.*
import com.brown.kaew.keepnote.adapters.NoteListAdapter
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.data.NoteDatabase
import com.brown.kaew.keepnote.data.NoteRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NoteFragmentViewModel(private val repository: NoteRepository) : ViewModel() {

    var i = 0
    val adapter = NoteListAdapter()
    val notes = repository.getAllNotes()

    init {
        Log.i(this.javaClass.simpleName, "init = $i")
        i += 1
    }

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

    fun saveInAdapterChanged() {

    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        Log.i(this.javaClass.simpleName, "onCleared()")
        viewModelScope.cancel()
    }
}