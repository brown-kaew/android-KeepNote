package com.brown.kaew.keepnote

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brown.kaew.keepnote.data.NoteDatabase
import com.brown.kaew.keepnote.data.NoteRepository
import com.brown.kaew.keepnote.viewmodels.NoteViewModel

object InjectorUtils {

    private fun getNoteRepository(context: Context): NoteRepository {
        return NoteRepository.getInstance(
            NoteDatabase.getInstance(context.applicationContext).noteDao()
        )
    }

    fun provideNoteViewModelFactory(context: Context): NoteViewModelFactory {
        return NoteViewModelFactory(getNoteRepository(context))
    }


}

class NoteViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NoteViewModel(noteRepository) as T
    }
}