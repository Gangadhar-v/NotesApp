package com.example.to_do.mvvm

import androidx.lifecycle.LiveData
import com.example.to_do.room.Note
import com.example.to_do.room.NoteDao

class NoteRepository(val noteDao: NoteDao) {

   suspend fun insertNote(note: Note){
            noteDao.insertNote(note)
    }
    suspend fun updateNote(note: Note){
        noteDao.updateNote(note)
    }
    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(note)
    }
    suspend fun deleteAll(){
        noteDao.deleteAll()
    }

    fun allNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }
}