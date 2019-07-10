package com.brown.kaew.keepnote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.brown.kaew.keepnote.databinding.FragmentNoteEditorBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import com.brown.kaew.keepnote.viewmodels.NoteEditorViewModel

class NoteEditorFragment : Fragment() {


    private val viewModel: NoteEditorViewModel by lazy {
        InjectorUtils.provideNoteEditorViewModelFactory(requireContext()).create(NoteEditorViewModel::class.java)
    }
    private lateinit var binding: FragmentNoteEditorBinding

//    companion object {
//        fun newInstance() = NoteEditorFragment()
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.saveNote()
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(this.javaClass.simpleName, "onDetach()")

    }


}
