package com.brown.kaew.keepnote.utilities

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brown.kaew.keepnote.data.NoteDatabase
import com.brown.kaew.keepnote.data.NoteRepository
import com.brown.kaew.keepnote.viewmodels.NoteEditorViewModel
import com.brown.kaew.keepnote.viewmodels.NoteFragmentViewModel

object InjectorUtils {

    fun getNoteRepository(context: Context): NoteRepository {
        return NoteRepository.getInstance(
            NoteDatabase.getInstance(context.applicationContext).noteDao()
        )
    }

    fun provideNoteViewModelFactory(context: Context): NoteViewModelFactory {
        return NoteViewModelFactory(getNoteRepository(context))
    }

    fun provideNoteEditorViewModelFactory(context: Context, noteId: Int): NoteEditorViewModelFactory {
        return NoteEditorViewModelFactory(getNoteRepository(context), noteId)
    }
}

class NoteViewModelFactory(private val noteRepository: NoteRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NoteFragmentViewModel(noteRepository) as T
    }
}

class NoteEditorViewModelFactory(
    private val noteRepository: NoteRepository,
    private val noteId: Int
) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NoteEditorViewModel(noteRepository, noteId) as T
    }
}