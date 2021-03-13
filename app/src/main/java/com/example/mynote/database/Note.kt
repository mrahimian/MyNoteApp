package com.example.mynote.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Parcelize
@Entity(tableName = "notes")
data class Note(
    @ColumnInfo
    var title: String?,
    @ColumnInfo(name = "description")
    var text: String?,
    @ColumnInfo
    var pointed : Int = 0

):Parcelable{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    override fun toString(): String {
        return "title : $title , text : $text , id = $id"
    }
}