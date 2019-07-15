package com.brown.kaew.keepnote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.brown.kaew.keepnote.databinding.FragmentNoteEditorBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import com.brown.kaew.keepnote.utilities.NoteEditorViewModelFactory
import com.brown.kaew.keepnote.viewmodels.NoteEditorFragmentViewModel

class NoteEditorFragment : Fragment() {

    private val args by navArgs<NoteEditorFragmentArgs>()
    private val isNewNote: Boolean by lazy { args.noteId == -1L }
    private val viewModel: NoteEditorFragmentViewModel by lazy {
        //        InjectorUtils.provideNoteEditorViewModelFactory(requireContext(), args.noteId)
//            .create(NoteEditorFragmentViewModel::class.java)
        ViewModelProviders.of(this,NoteEditorViewModelFactory(InjectorUtils.getNoteRepository(requireContext()),args.noteId) )
            .get(NoteEditorFragmentViewModel::class.java)
    }
    private lateinit var binding: FragmentNoteEditorBinding

//    companion object {
//        fun newInstance() = NoteEditorFragment()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(this.javaClass.simpleName, "onCreateView() id= ${args.noteId} $isNewNote")

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_note_editor,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@NoteEditorFragment

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.javaClass.simpleName, "call saveNote()")
        viewModel.saveNote()
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(this.javaClass.simpleName, "onDetach()")

    }
}
