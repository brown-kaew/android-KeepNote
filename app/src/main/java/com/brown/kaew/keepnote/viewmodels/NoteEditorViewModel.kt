package com.brown.kaew.keepnote.viewmodels

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.data.NoteRepository
import kotlinx.coroutines.launch

class NoteEditorViewModel(private val repository: NoteRepository) : ViewModel() {

    val title = MutableLiveData<String>("")
    val note = MutableLiveData<String>("")

    fun saveNote() {
        val titleLen = title.value.toString().length
        val noteLen = note.value.toString().length

        if (titleLen != 0 && noteLen != 0) {
            viewModelScope.launch {
                repository.insertNote(
                    Note(title.value.toString(), note.value.toString())
                )
            }
        }
    }
}
