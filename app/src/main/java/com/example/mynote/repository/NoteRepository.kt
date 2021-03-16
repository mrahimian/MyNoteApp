package com.example.mynote.repository

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mynote.database.Note
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteDatabase

class NoteRepository {
    private var noteDao: NoteDao
    private var allNotes: LiveData<List<Note>>
    private var allPointedNotes: LiveData<List<Note>>

    constructor(application: Application) {
        val database = NoteDatabase.getDatabase(application)
        noteDao = database.noteDao()
        allNotes = noteDao.getNotes()
        allPointedNotes = noteDao.getPointedNotes()
    }

    fun insert(note: Note) {
        InsertNote(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateNote(noteDao).execute(note)
    }

    fun updatePoint(note: Note, point: Int) {
        UpdatePoint(noteDao, point).execute(note)
    }

    fun delete(note: Note) {
        DeleteNote(noteDao).execute(note)
    }

    fun deleteSelected() {
        DeleteSelected(noteDao).execute()
    }


    fun deleteAllNotes() {
        DeleteAllNotes(noteDao).execute()
    }

    fun getNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun getPointedNotes(): LiveData<List<Note>> {
        return allPointedNotes
    }

    companion object {
        private class InsertNote(val noteDao: NoteDao) : AsyncTask<Note, Void?, Void?>() {
            override fun doInBackground(vararg params: Note?): Void? {
                params[0]?.let { noteDao.insert(it) }
                return null
            }
        }

        private class UpdateNote(val noteDao: NoteDao) : AsyncTask<Note, Void?, Void?>() {
            override fun doInBackground(vararg params: Note?): Void? {
                params[0]?.let { noteDao.update(it) }
                return null
            }
        }

        private class UpdatePoint(val noteDao: NoteDao, val point: Int) :
            AsyncTask<Note, Void?, Void?>() {
            override fun doInBackground(vararg params: Note?): Void? {
                params[0]?.let { it.id?.let { it1 -> noteDao.updatePoint(it1, point) } }
                return null
            }
        }

        private class DeleteNote(val noteDao: NoteDao) : AsyncTask<Note, Void?, Void?>() {
            override fun doInBackground(vararg params: Note?): Void? {
                params[0]?.let { noteDao.delete(it) }
                return null
            }
        }

        private class DeleteAllNotes(val noteDao: NoteDao) : AsyncTask<Void, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                noteDao.deleteAllNotes()
                return null
            }
        }

        private class DeleteSelected(val noteDao: NoteDao) : AsyncTask<Void, Void?, Void?>() {
            override fun doInBackground(vararg params: Void?): Void? {
                /*for (i in noteDao.getNotes().value!!){
                    Log.e("items1", i.toString())
                }*/
                noteDao.deleteSelected()
                return null
            }
        }


    }
}