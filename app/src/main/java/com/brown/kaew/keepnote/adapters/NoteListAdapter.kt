package com.brown.kaew.keepnote.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.brown.kaew.keepnote.NoteEditorFragmentArgs
import com.brown.kaew.keepnote.NoteFragmentDirections
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.R
import com.brown.kaew.keepnote.databinding.CardNoteBinding

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var noteList = emptyList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
//        Log.i(this.javaClass.simpleName, "onCreateView View Type = $viewType")
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_note, parent, false)
//        return NoteListViewHolder(view)
        return NoteListViewHolder(CardNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
//        Log.i(this.javaClass.simpleName, "onBindView")
        val currentNote: Note = noteList[position]
        holder.apply {
            bind(View.OnClickListener {
                val direction = NoteFragmentDirections.actionNoteFragmentToNoteEditorFragment(currentNote.nId)
                it.findNavController().navigate(direction)
                Log.i(this.javaClass.simpleName, "onBindView holderClick")
            }, currentNote)
        }

    }

    fun updateNote(list: List<Note>) {
        this.noteList = list
        Log.i(this.javaClass.simpleName, "note size = ${noteList.size}")
        notifyDataSetChanged()
    }

    class NoteListViewHolder(
        private val binding: CardNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Note) {
            binding.clickListener = listener
            binding.note = item
        }
    }
}
