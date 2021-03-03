package com.example.mynote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mynote.database.Note
import com.example.mynote.repository.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository : NoteRepository = NoteRepository(application)
    private val allNotes  = repository.getNotes()
    private val allPointedNotes = repository.getPointedNotes()

    fun insert(note: Note){
        repository.insert(note)
    }

    fun update(note: Note){
        repository.update(note)
    }

    fun delete(note: Note){
        repository.delete(note)
    }

    fun deleteAllNotes(){
        repository.deleteAllNotes()
    }

    fun getNotes() : LiveData<List<Note>>{
        return allNotes
    }

    fun getPointedNotes() : LiveData<List<Note>>{
        return allPointedNotes
    }



}