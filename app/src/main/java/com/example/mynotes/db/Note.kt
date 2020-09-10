package com.example.mynotes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note")
data class Note(

    @ColumnInfo(name = "Note title")
    val title: String,

    @ColumnInfo(name = "Note content")
    val note: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


) : Serializable
