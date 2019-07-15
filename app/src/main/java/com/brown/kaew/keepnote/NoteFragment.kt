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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.brown.kaew.keepnote.adapters.NoteListAdapter
import com.brown.kaew.keepnote.databinding.FragmentNoteBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import com.brown.kaew.keepnote.utilities.NoteViewModelFactory
import com.brown.kaew.keepnote.viewmodels.NoteFragmentViewModel

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
//            val noteListAdapter = NoteListAdapter()
//            listNotes.adapter = noteListAdapter

            //apply adapter from viewModel
            listNotes.adapter = noteFragmentViewModel.adapter

            //Observe data
            noteFragmentViewModel.notes.observe(this@NoteFragment, Observer {
//                noteListAdapter.updateNote(it)
                noteFragmentViewModel.adapter.updateNote(it)
            })

            //start Action Mode if actionMode in adapter not null
            noteFragmentViewModel.adapter.startActionMode(requireActivity())

        }
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        noteFragmentViewModel.saveInAdapterChanged()
    }


    /* private val onClickListener: (View, Int, Int) -> Unit = { view, position, type ->
         run { Log.i(this.javaClass.simpleName, "onClick()") }
     }

     private val onLongClickListener: (View, Int, Int) -> Boolean = { view, position, type ->
         run {
             Log.i(this.javaClass.simpleName, "onLongClick()")
             // Called when the user long-clicks on someView
             when (actionMode) {
                 null -> {
                     // Start the CAB using the ActionMode.Callback defined above
                     actionMode = NoteActionModeCallback().startActionMode(view, R.menu.fragment_note_menu)
 //                            it.apply {
 //                                isSelected = !isSelected
 //                                if (isSelected) {
 //                                    setBackgroundResource(R.drawable.bg_rounded_white_with_border)
 //                                } else {
 //                                    setBackgroundResource(R.drawable.bg_rounded_white)
 //                                }
 //                            }
                     true
                 }
                 else -> false
             }
         }

     }

     private val actionModeCallback = object : ActionMode.Callback {
         // Called when the action mode is created; startActionMode() was called
         override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
             // Inflate a menu resource providing context menu items
             val inflater: MenuInflater = mode.menuInflater
             inflater.inflate(R.menu.fragment_note_menu, menu)
             return true
         }

         // Called each time the action mode is shown. Always called after onCreateActionMode, but
         // may be called multiple times if the mode is invalidated.
         override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
             return false // Return false if nothing is done
         }

         // Called when the user selects a contextual menu item
         override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
             return when (item.itemId) {
                 R.id.action_delete -> {
 //                    shareCurrentItem()
                     mode.finish() // Action picked, so close the CAB
                     true
                 }
                 else -> false
             }
         }

         // Called when the user exits the action mode
         override fun onDestroyActionMode(mode: ActionMode) {
 //            notifyDataSetChanged()
             Log.i(this.javaClass.simpleName, "onDestroyActionMode")
             actionMode = null
         }
     }*/

}
