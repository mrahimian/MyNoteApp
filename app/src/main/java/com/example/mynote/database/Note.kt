package com.example.mynote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @ColumnInfo
    val title: String?,
    @ColumnInfo(name = "description")
    val text: String?,

){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo
    var pointed : Int = 0

    override fun toString(): String {
        return "title : $title , text : $text , id = $id"
    }
}