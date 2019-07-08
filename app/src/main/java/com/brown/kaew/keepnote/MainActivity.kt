package com.brown.kaew.keepnote

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.brown.kaew.keepnote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private val noteViewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel(application)::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = mViewModel
        binding.apply {

            // take a note
            tvTakeNote.setOnClickListener { view ->
                noteViewModel.insertNotes(
                    Note("testFromActivity", "Holaddadada")
                )
            }

            //RecyclerView
            listNotes.setHasFixedSize(true)
            listNotes.layoutManager = StaggeredGridLayoutManager(
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3,
                StaggeredGridLayoutManager.VERTICAL
            )
            val noteListAdapter = NoteListAdapter()
            listNotes.adapter = noteListAdapter

            //Observe data
            noteViewModel.getAllNote().observe(this@MainActivity, Observer {
                noteListAdapter.updateNote(it)
            })


        }

    }
}
