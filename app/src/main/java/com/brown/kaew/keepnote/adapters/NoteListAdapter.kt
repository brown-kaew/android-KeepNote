package com.brown.kaew.keepnote.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.R

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var noteList = emptyList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        Log.d("viewType", "$viewType")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_note, parent, false)
        return NoteListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.tvCardTitle.text = currentNote.title
        holder.tvCardNote.text = currentNote.note
    }

    fun updateNote(list: List<Note>) {
        this.noteList = list
        Log.i(this.javaClass.simpleName, "note size = ${noteList.size}")
        notifyDataSetChanged()
    }

    class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCardTitle: TextView = itemView.findViewById<TextView>(R.id.tv_card_title)
        val tvCardNote: TextView = itemView.findViewById<TextView>(R.id.tv_card_note)
    }
}