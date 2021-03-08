package com.example.mynote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.R
import com.example.mynote.database.Note
import java.text.FieldPosition

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    private lateinit var listener: OnClickListener
    private var notes : List<Note> = arrayListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteHolder {
        val itemView = LayoutInflater.from(p0.context)
            .inflate(R.layout.summary_show,p0,false)

        return NoteHolder(itemView,listener)
    }

    override fun onBindViewHolder(p0: NoteHolder, p1: Int) {
        val note = notes[p1]
        p0.title.text = note.title
        p0.description.text = note.text
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setNotes(notes : List<Note>){
        this.notes = notes
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int) : Note{
        return notes[position]
    }

    inner class NoteHolder(itemView: View, listener: OnClickListener) : RecyclerView.ViewHolder(itemView){
        var title : TextView = itemView.findViewById(R.id.note_title)
        var description : TextView = itemView.findViewById(R.id.note_description)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onClick(notes[position])
                }
            }
        }
    }

    interface OnClickListener{
        fun onClick(note: Note)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.listener = onClickListener
    }
}