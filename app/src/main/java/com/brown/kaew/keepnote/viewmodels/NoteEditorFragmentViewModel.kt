package com.brown.kaew.keepnote.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.data.NoteRepository
import kotlinx.coroutines.*
import java.text.DateFormat
import java.util.*

class NoteEditorFragmentViewModel(
    private val repository: NoteRepository,
    private val noteId: Long
) : ViewModel() {

    private val isNewNote: Boolean by lazy { noteId == -1L }
    private var _calendar = Calendar.getInstance()
    private val _time = MutableLiveData<String>(formatDate(_calendar))

    val title = MutableLiveData<String>("")
    val note = MutableLiveData<String>("")
    val time: LiveData<String> = _time

    private var insertedNote: Note = Note()
    private val copiedNote = insertedNote


    init {
        if (!isNewNote) {
            viewModelScope.launch(Dispatchers.Main) {
                insertedNote = repository.getById2(noteId)
                title.value = insertedNote.title
                note.value = insertedNote.note
                _time.value = formatDate(insertedNote.date)
            }
        }
    }

    fun saveNote() {
        val titleLen = title.value.toString().length
        val noteLen = note.value.toString().length

        //if isNewNote then insert or update otherwise update it
        if ((titleLen != 0 || noteLen != 0) && isNewNote) {
            //Because configuration change will call saveNote() (this function scope) in onStop()
            //so, it need ensure this new note will be added only once if no data change.
            //Otherwise update it
            if (insertedNote == copiedNote) {
                //add new note
                viewModelScope.launch(Dispatchers.IO) {
                    runBlocking {
                        Log.i(this.javaClass.simpleName, "adding new note")
                        val id = repository.insertNote(
                            Note(title.value.toString(), note.value.toString(), Calendar.getInstance())
                        )
                        Log.i(this.javaClass.simpleName, "added new note")
                        //keep last added note
                        insertedNote = repository.getById2(id)
                        Log.i(this.javaClass.simpleName, "keep last added note")
                    }
                }

            } else {
                //update new note
                Log.i(this.javaClass.simpleName, "update new note")
                insertedNote.title = title.value.toString()
                insertedNote.note = note.value.toString()
                insertedNote.date = Calendar.getInstance()

                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateNote(insertedNote)
                }
            }


        } else if (isNoteChanged(insertedNote)) {
            Log.i(this.javaClass.simpleName, "update note")

            viewModelScope.launch(Dispatchers.IO) {
                runBlocking {
                    //prepare note
                    val noteTmp = repository.getById2(noteId)
                    noteTmp.title = title.value.toString()
                    noteTmp.note = note.value.toString()
                    noteTmp.date = Calendar.getInstance()

                    //update note
                    repository.updateNote(noteTmp)
                }
            }
        }
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


    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        Log.i(this.javaClass.simpleName, "onCleared()")
        viewModelScope.cancel()
    }

}
