package com.brown.kaew.keepnote

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.brown.kaew.keepnote.databinding.FragmentNoteBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import com.brown.kaew.keepnote.utilities.NoteViewModelFactory
import com.brown.kaew.keepnote.viewmodels.NoteFragmentViewModel
import java.util.*

class NoteFragment : Fragment() {

    private val noteFragmentViewModel: NoteFragmentViewModel by lazy {
        //        InjectorUtils.provideNoteViewModelFactory(requireContext()).create(NoteFragmentViewModel::class.java)
        ViewModelProviders.of(this, NoteViewModelFactory(InjectorUtils.getNoteRepository(requireContext())))
            .get(NoteFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(this.javaClass.simpleName, "onCreate")
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNoteBinding>(inflater, R.layout.fragment_note, container, false)
        binding.lifecycleOwner = this@NoteFragment
        binding.viewModel = noteFragmentViewModel
        binding.apply {

            //RecyclerView
            listNotes.setHasFixedSize(true)
            listNotes.layoutManager = StaggeredGridLayoutManager(
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3,
                StaggeredGridLayoutManager.VERTICAL
            )

            //apply adapter from viewModel
            listNotes.adapter = noteFragmentViewModel.adapter

            //Observe data
            noteFragmentViewModel.notes.observe(this@NoteFragment, Observer {
                Log.i(this.javaClass.simpleName,"observe list")
                noteFragmentViewModel.adapter.updateNote(it)
            })

            // add new note button
            tvTakeNote.setOnClickListener { view ->
                //close contextual action bar before navigate
                noteFragmentViewModel.adapter.requireDestroyActionMode()
                view.findNavController().navigate(R.id.action_noteFragment_to_noteEditorFragment)
            }

            //start Action Mode if actionMode in adapter not null
            noteFragmentViewModel.adapter.shouldStartActionMode(requireActivity())

        }
        return binding.root
    }

    private fun showLogI(string: String) {
        Log.i(this.javaClass.simpleName, string)
    }

}
