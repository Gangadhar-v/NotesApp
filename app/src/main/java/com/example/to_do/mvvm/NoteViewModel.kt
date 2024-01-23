package com.example.to_do.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.room.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository:NoteRepository):ViewModel() {

    val inputTitle =MutableLiveData<String>()
    val inputDesc =MutableLiveData<String>()
    val displayAllnotes=noteRepository.allNotes()

    private val noteEventChannel =Channel<NoteEvent>()
    val noteEvent = noteEventChannel.receiveAsFlow()

    fun addNote(){
        val title=inputTitle.value.toString()
        val desc=inputDesc.value.toString()
        insertNote(Note(title=title!!,desc=desc!!,id=0))
    }

    fun insertNote(note: Note){
        viewModelScope.launch {
            Dispatchers.IO
            noteRepository.insertNote(note=note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch {
            Dispatchers.IO
            noteRepository.updateNote(note=note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            Dispatchers.IO
            noteRepository.deleteNote(note=note)
            noteEventChannel.send(NoteEvent.ShowUndoDeleteMessage(note))
        }
    }
    fun deleteAll(){
        viewModelScope.launch {
            Dispatchers.IO
            noteRepository.deleteAll()
        }
    }
    fun editNote(note:Note){
        updateNote(note)
    }
    sealed class NoteEvent{
        data class ShowUndoDeleteMessage(val note:Note):NoteEvent()
    }

    fun onUndoDeleteClick(note:Note){
        viewModelScope.launch { insertNote(note) }
    }
}