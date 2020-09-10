package com.example.mynotes.ui

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.example.mynotes.R
import com.example.mynotes.db.Note
import com.example.mynotes.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast


class AddNoteFragment : BaseFragment() {

    private var note: Note? = null
    private var priority = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            editText_title.setText(note?.title)
            editText_inputNote.setText(note?.note)
        }
        btn_saveNote.setOnClickListener { view ->
            val noteTitle = editText_title.text.toString().trim()
            val noteContent = editText_inputNote.text.toString().trim()
            if (noteTitle.isEmpty()) {
                editText_title.error = "Title doesn't required"
                editText_title.requestFocus()
                return@setOnClickListener
            }
            if (noteContent.isEmpty()) {
                editText_inputNote.error = "Content doesn't required"
                editText_inputNote.requestFocus()
                return@setOnClickListener
            }

            launch {
                context?.let { it ->
                    val mNote = Note(noteTitle, noteContent)
                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        // Toast.makeText(activity, "Note saved", Toast.LENGTH_SHORT).show()

                        MotionToast.createToast(
                            context as Activity, "Note Saved",
                            MotionToast.TOAST_INFO,
                            MotionToast.GRAVITY_CENTER,
                            MotionToast.SHORT_DURATION,
                            context?.let { ResourcesCompat.getFont(it, R.font.helvetica_regular) }
                        )
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        // Toast.makeText(activity, "Note updated", Toast.LENGTH_SHORT).show()
                        MotionToast.createToast(
                            context as Activity, "Note Updated",
                            MotionToast.TOAST_INFO,
                            MotionToast.GRAVITY_CENTER,
                            MotionToast.SHORT_DURATION,
                            context?.let { ResourcesCompat.getFont(it, R.font.helvetica_regular) }
                        )
                    }


                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }

            }


        }
    }


}