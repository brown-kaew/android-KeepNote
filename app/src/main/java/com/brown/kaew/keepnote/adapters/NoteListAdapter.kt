package com.brown.kaew.keepnote.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.brown.kaew.keepnote.NoteFragmentDirections
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.R
import com.brown.kaew.keepnote.databinding.CardNoteBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import kotlinx.coroutines.*

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var noteList = emptyList<Note>()
    private var selectedPosition = arrayListOf<Int>()
    private var actionMode: ActionMode? = null
    private lateinit var parentContext: Context

    init {
        Log.i(this.javaClass.simpleName, "init")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        parentContext = parent.context
        return NoteListViewHolder(CardNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
//        Log.i(this.javaClass.simpleName, "onBindView")
        val currentNote: Note = noteList[position]

        holder.apply {
            bind(
                View.OnClickListener {
                    when (actionMode) {
                        null -> { // navigate to its page
                            val direction =
                                NoteFragmentDirections.actionNoteFragmentToNoteEditorFragment(currentNote.nId)
                            it.findNavController().navigate(direction)
                            Log.i(this.javaClass.simpleName, "onBindView holderClick")
                        }
                        else -> {
                            it.apply {
                                onItemSelected(position)
                            }

                        }
                    }
                },
                View.OnLongClickListener {
                    // Called when the user long-clicks on someView
                    when (actionMode) {
                        null -> {
                            // Start the CAB using the ActionMode.Callback defined above
                            actionMode = it.startActionMode(actionModeCallback)
                            it.apply {
                                onItemSelected(position)
                            }
                            true
                        }
                        else -> false
                    }
                },
                noteList[position]
            )
        }
    }

    private fun onItemSelected(position: Int) {
        noteList[position].isSelected = !noteList[position].isSelected

        if (noteList[position].isSelected) {
            selectedPosition.add(position)
        } else {
            selectedPosition.remove(position)
        }
        actionMode!!.title = selectedPosition.size.toString()
        Log.i("selectedList", "$selectedPosition")
        notifyItemChanged(position)
    }

    private fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, adapterPosition, itemViewType)
        }
        return this
    }

    private fun <T : RecyclerView.ViewHolder> T.onLongClick(event: (view: View, position: Int, type: Int) -> Boolean): T {
        itemView.setOnLongClickListener {
            event.invoke(it, adapterPosition, itemViewType)
            true
        }
        return this
    }


    fun updateNote(list: List<Note>) {
        this.noteList = list
        Log.i(this.javaClass.simpleName, "note size = ${noteList.size}")
        notifyDataSetChanged()
    }

    //call from activity or fragment
    fun startActionMode(activity: Activity) {
        if (actionMode != null) {
            activity.startActionMode(actionModeCallback)
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
            return false
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {

                    deleteNotes()

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
            if (selectedPosition.size > 0) {
                for (position in selectedPosition) {
                    noteList[position].isSelected = false
                    notifyItemChanged(position)
                }
                selectedPosition.clear()
            }
            actionMode = null
        }
    }

    private fun deleteNotes() {
        val repository = InjectorUtils.getNoteRepository(parentContext)
        if(selectedPosition.size>0) {
            runBlocking(Dispatchers.IO) {
                for (position in selectedPosition) {
                    repository.deleteNote(noteList[position])
                    notifyItemChanged(position)
                }
            }
            selectedPosition.clear()
        }
    }

    class NoteListViewHolder(
        private val binding: CardNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, longClickListener: View.OnLongClickListener, item: Note) {
            binding.clickListener = listener
            binding.longClickListener = longClickListener
            binding.note = item

            item.apply {
                if (item.isSelected) {
                    binding.root.setBackgroundResource(R.drawable.bg_rounded_white_with_border)
                } else {
                    binding.root.setBackgroundResource(R.drawable.bg_rounded_white)
                }
            }

//            binding.executePendingBindings()
        }
    }
}
