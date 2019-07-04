package com.brown.kaew.keepnote

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private val noteList = listOf(
        Note("11111", "aaaaaa"),
        Note("22222", "simply dummy text of the printing and typeset a gap eleaseinto electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the r of Letraset sheets"),
        Note("33333", "ng industry. "),
        Note("44444", "lley of ty"),
        Note("55555", "into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the r"),
        Note("66666", "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took")
    )

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


    class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCardTitle: TextView = itemView.findViewById<TextView>(R.id.tv_card_title)
        val tvCardNote: TextView = itemView.findViewById<TextView>(R.id.tv_card_note)


    }
}