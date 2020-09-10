package com.example.mynotes.ui

import android.app.Activity
import android.content.DialogInterface

import android.os.Bundle

import android.view.*

import androidx.appcompat.app.AlertDialog

import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mynotes.R
import com.example.mynotes.adapters.NoteAdapter
import com.example.mynotes.db.Note
import com.example.mynotes.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast


class HomeFragment : BaseFragment(), CoroutineScope {


    private val adapter by lazy { NoteAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(context)


        launch {
            context?.let {
                val notes = NoteDatabase(it).getNoteDao().getAllNotes()
                if (notes.isEmpty()) {
                    imgAddNoteIcon.visibility = View.VISIBLE
                    txtEmptyNoteList.visibility = View.VISIBLE
                } else {
                    imgAddNoteIcon.visibility = View.GONE
                    txtEmptyNoteList.visibility = View.GONE
                }
                recyclerView.adapter = adapter
                adapter.setData(notes as MutableList<Note>)
            }
        }

        btn_addNote.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote()
            Navigation.findNavController(it).navigate(action)

        }

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                launch {
                    context?.let {
                        var position = viewHolder.adapterPosition
                        val notes = NoteDatabase(context!!).getNoteDao().getAllNotes()
                        NoteDatabase(context!!).getNoteDao().deleteNoteById(notes[position].id)
                        adapter.removeNote(position)
                        MotionToast.createToast(
                            context as Activity, "Note deleted",
                            MotionToast.TOAST_DELETE,
                            MotionToast.GRAVITY_CENTER,
                            MotionToast.SHORT_DURATION,
                            context?.let { ResourcesCompat.getFont(it, R.font.helvetica_regular) }
                        )
                    }
                }


            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun showDialogAlert() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Delete All Notes")
        builder?.setMessage("Do you want to delete all notes?")
        builder?.setPositiveButton("Yes") { _, _ ->
            deleteAllNotes()
        }
        builder?.setNegativeButton("No") { _, _ ->

        }
        builder?.show()
    }

    private fun deleteAllNotes() {

        launch {
            NoteDatabase(requireContext()).getNoteDao().deleteAllNotes()
            adapter.clear()
            MotionToast.createToast(
                context as Activity, "All notes deleted!",
                MotionToast.TOAST_DELETE,
                MotionToast.GRAVITY_CENTER,
                MotionToast.SHORT_DURATION,
                context?.let { ResourcesCompat.getFont(it, R.font.helvetica_regular) }
            )

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_deleteAllNotes -> showDialogAlert()
        }



        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_deleteall, menu)
    }


}


