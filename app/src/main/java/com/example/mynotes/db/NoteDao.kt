package com.example.mynotes.db

import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun addNote(note: Note)


    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM note WHERE id=:id")
    fun deleteNoteById(id:Int)

    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()


}