package com.brown.kaew.keepnote

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.brown.kaew.keepnote.adapters.NoteListAdapter
import com.brown.kaew.keepnote.databinding.FragmentNoteBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import com.brown.kaew.keepnote.viewmodels.NoteViewModel

class NoteFragment : Fragment() {

    private val noteViewModel: NoteViewModel by lazy {
        InjectorUtils.provideNoteViewModelFactory(requireContext()).create(NoteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentNoteBinding>(inflater, R.layout.fragment_note, container, false)
        binding.lifecycleOwner = this@NoteFragment
        binding.apply {

            // take a note
            tvTakeNote.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_noteFragment_to_noteEditorFragment)
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
            noteViewModel.getAllNote().observe(this@NoteFragment, Observer {
                noteListAdapter.updateNote(it)
            })

        }

        // Inflate the layout for this fragment
        return binding.root
    }

}
