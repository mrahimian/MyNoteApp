package com.example.mynote.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun insert(note : Note)

    @Update
    fun update(note : Note)

    @Query("update notes set pointed = :point where id = :id ")
    fun updatePoint(id : Int, point : Int)

    @Delete
    fun delete(note : Note)

    @Query("delete from notes")
    fun deleteAllNotes()

    @Query("select * from notes ")
    fun getNotes() : LiveData<List<Note>>

    @Query("select * from notes where pointed = 1")
    fun getPointedNotes() : LiveData<List<Note>>
}