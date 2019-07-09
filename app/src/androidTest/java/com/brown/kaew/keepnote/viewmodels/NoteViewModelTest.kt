package com.brown.kaew.keepnote.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.data.NoteDatabase
import com.brown.kaew.keepnote.data.NoteRepository
import com.brown.kaew.keepnote.utilities.getLastValue
import org.junit.*

class NoteViewModelTest {

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var viewModel: NoteViewModel

    private lateinit var note: List<Note>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() //Executes each task synchronously

    @Before
    fun setUp() {
        //Prepare DB and viewModel
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        noteDatabase = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java).build()

        val noteRepo = NoteRepository.getInstance(noteDatabase.noteDao())
        viewModel = NoteViewModel(noteRepo)

        //Prepare note for testing
        note = listOf(Note("test", "hello world"))
    }

    @After
    fun tearDown() {
        noteDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testInsertValue() {
        viewModel.insertListOfNotes(note)
        Assert.assertEquals(note[0], getLastValue(viewModel.getAllNote()))
    }

}