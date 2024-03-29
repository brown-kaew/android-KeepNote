package com.brown.kaew.keepnote.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.brown.kaew.keepnote.NoteFragmentDirections
import com.brown.kaew.keepnote.data.Note
import com.brown.kaew.keepnote.R
import com.brown.kaew.keepnote.databinding.CardNoteBinding
import com.brown.kaew.keepnote.utilities.InjectorUtils
import kotlinx.coroutines.*

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private val noteList = mutableListOf<Note>()
    private var selectedPosition = arrayListOf<Int>()
    private var actionMode: ActionMode? = null
    private lateinit var parentContext: Context

    init {
        Log.i(this.javaClass.simpleName, "init")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (actionMode == null) {
            ItemTouchHelper(_itemTouchHelperCallback).apply {
                attachToRecyclerView(recyclerView)
            }
        }
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
                            Log.i(this.javaClass.simpleName, "create action mode $actionMode")
                            it.apply {
                                onItemSelected(position)
                            }
                            true
                        }
                        else -> {
                            it.apply {
                                onItemSelected(position)
                            }
                            true
                        }
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

        if (selectedPosition.size == 0) {
            actionMode!!.finish()
        }
        notifyItemChanged(position)
    }


    fun updateNote(list: List<Note>) {
        this.noteList.apply {
            clear()
            addAll(list)
        }
        Log.i(this.javaClass.simpleName, "note size = ${noteList.size}")
        showList()
        notifyDataSetChanged()
    }

    fun shouldStartActionMode(activity: Activity) {
        if (actionMode != null) {
            actionMode = null
            actionMode = activity.startActionMode(actionModeCallback)
            actionMode!!.title = selectedPosition.size.toString()
            Log.i(this.javaClass.simpleName, "re-create action mode $actionMode")

        }
    }

    fun requireDestroyActionMode() {
        if (actionMode != null) {
            actionMode!!.finish()
            destroyActionMode()
        }
    }


    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created; shouldStartActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            mode.title = selectedPosition.size.toString()
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
            destroyActionMode()
        }
    }

    private fun destroyActionMode() {
        Log.i(this.javaClass.simpleName, "onDestroyActionMode $actionMode")
        if (selectedPosition.size > 0) {
            for (position in selectedPosition) {
                noteList[position].isSelected = false
                notifyItemChanged(position)
            }
            selectedPosition.clear()
        }
        actionMode = null
    }

    private fun deleteNotes() {
        val repository = InjectorUtils.getNoteRepository(parentContext)
        if (selectedPosition.size > 0) {
            runBlocking(Dispatchers.IO) {
                for (position in selectedPosition) {
                    repository.deleteNote(noteList[position])
                    notifyItemChanged(position)
                }
            }
            selectedPosition.clear()
        }
    }

    private fun updateDB() {
        val repository = InjectorUtils.getNoteRepository(parentContext)
        val prepareList = mutableListOf<Note>()

        for (item in noteList) {
            prepareList.add((Note(item.title, item.note, item.date)))
        }

        prepareList.reverse()

        runBlocking {
            repository.deleteAllAndInsert(prepareList)
        }
    }

    private fun showLogI(string: String) {
        Log.i(this.javaClass.simpleName, string)
    }

    private val _itemTouchHelperCallback = object : ItemTouchHelper.Callback() {
        private var isMoved = false
        private var mFromIndex: Int = -1
        private var toIndex = -1

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            showLogI("getMovementFlags")
            return makeFlag(
                ItemTouchHelper.ACTION_STATE_DRAG,
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
            )
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            showLogI("onMove")
            val fromIndex = viewHolder.adapterPosition
            toIndex = target.adapterPosition

            if (!isMoved) {
                mFromIndex = fromIndex
            }

            //clear selected item
            if (selectedPosition.size > 0) {
                for (position in selectedPosition) {
                    noteList[position].isSelected = false
                    if (mFromIndex != position) {
                        notifyItemChanged(position)
                    }
                }
                selectedPosition.clear()
            }

            showLogI("lists $fromIndex to $toIndex")


            // notify move
            notifyItemMoved(fromIndex, toIndex)

            //close contextual appbar
            requireDestroyActionMode()

            isMoved = true

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            showLogI("onSwiped")
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)

            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                showLogI("onSelectedDragged")

            } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && isMoved) {
                isMoved = false
                showLogI("onSelectedDropped")

                //update list order
                val movedItem = noteList[mFromIndex]
                noteList.remove(movedItem)
                showList()
                noteList.add(toIndex, movedItem)
                showList()

                notifyItemChanged(toIndex)
                updateDB()
            }
        }

    }

    private fun showList() {
        var text = ""
        for (item in noteList) {
            text = "$text   ${item.title}"
        }
        showLogI("lists $text")
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

            binding.executePendingBindings()
        }
    }
}
