package com.example.to_do.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name="note_title")
    val title:String,
    @ColumnInfo(name="note_desc")
    val desc:String
)
