package com.example.mynotes.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.db.Note
import com.example.mynotes.ui.HomeFragmentDirections
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.list_note.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

class NoteAdapter :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var notes: MutableList<Note>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_note, parent, false)

        return ViewHolder(view)
    }

    fun setData(data: MutableList<Note>) {
        this.notes = data
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.txt_noteTitle.text = notes[position].title
        holder.itemView.txt_noteContent.text = notes[position].note
        holder.itemView.imageIcon.setImageResource(R.drawable.ic_notebook)
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote()
            action.note = notes[position]
            Navigation.findNavController(it).navigate(action)
        }


    }

    private fun getImage(priority: Int): Int =
        if (priority == 1) R.drawable.low_priority else if (priority == 2) R.drawable.medium_priority else R.drawable.high_priority

    override fun getItemCount(): Int {
        return notes.size
    }

    fun removeNote(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        notes.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {


    }


}