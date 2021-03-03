package com.example.mynote.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note : Note)

    @Update
    fun update(note : Note)

    @Delete
    fun delete(note : Note)

    @Query("delete from notes")
    fun deleteAllNotes()

    @Query("select * from notes ")
    fun getNotes() : LiveData<List<Note>>

    @Query("select * from notes where pointed = 1")
    fun getPointedNotes() : LiveData<List<Note>>
}