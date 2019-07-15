package com.brown.kaew.keepnote.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.data.NoteRepository
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class NoteEditorViewModel(
    private val repository: NoteRepository,
    private val noteId: Int
) : ViewModel() {

    private val isNewNote: Boolean by lazy { noteId == -1 }
    private var _calendar = Calendar.getInstance()
    private val _time = MutableLiveData<String>(formatDate(_calendar))

    val title = MutableLiveData<String>("")
    val note = MutableLiveData<String>("")
    val time: LiveData<String> = _time

    private var oldNote: Note = Note()

    init {

    }

    fun getById(id: Int) = repository.getById(id)

    fun saveNote() {
        val titleLen = title.value.toString().length
        val noteLen = note.value.toString().length

        //if new note then insert otherwise update it
        if ((titleLen != 0 || noteLen != 0) && isNewNote) {
            val newNote = Note(title.value.toString(), note.value.toString(), Calendar.getInstance())

            //Because configuration change will call saveNote() (this function scope) in onStop()
            //so, it need to compare between old and new one to ensure this note will not save only once if no data change.
            if (isNoteChanged(oldNote)) {
                Log.i(this.javaClass.simpleName, "add new note")
                viewModelScope.launch {
                    repository.insertNote(
                        newNote
                    )
                }
                oldNote = newNote
            }
        } else if (isNoteChanged(oldNote)) {
            Log.i(this.javaClass.simpleName, "update note")
            viewModelScope.launch {
                repository.updateNote(
                    Note(title.value.toString(), note.value.toString(), Calendar.getInstance()).apply {
                        nId = noteId
                    }
                )
            }
        }
    }


    fun mapNote(it: Note) {
        oldNote = it
        title.value = it.title
        note.value = it.note
        _time.value = formatDate(it.date)
//            it.date.getDisplayName(Calendar.MONTH, Calendar.ALL_STYLES, Locale.US)
    }

    private fun isNoteChanged(oldNote: Note): Boolean {
        val newNote = Note(title.value!!, note.value!!, oldNote.date).apply {
            nId = noteId
        }
        return oldNote != newNote //If note equal that mean it has changed
    }

    private fun formatDate(calendar: Calendar): String {
//        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.ALL_STYLES, Locale.US)
//        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.ALL_STYLES, Locale.US)
        val time = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.US).format(calendar.time)
//        val timePf = calendar.getDisplayName(Calendar.AM_PM, Calendar.ALL_STYLES, Locale.US)


//        return "$month $dayOfWeek $time $timePf"
        return time
    }

}
