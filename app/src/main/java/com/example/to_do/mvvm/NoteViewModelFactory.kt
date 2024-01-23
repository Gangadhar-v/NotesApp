package com.example.to_do.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


//IF THEIR IS A CONSTRUCTOR PARAMETER IN VIEWMODEL THEN USE VIEWMODEL FACTORY
class NoteViewModelFactory(private val repo:NoteRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)){
            return NoteViewModel(noteRepository = repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}