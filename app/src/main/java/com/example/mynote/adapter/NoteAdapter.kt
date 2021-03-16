package com.example.mynote.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.compose.ui.graphics.Color
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.R
import com.example.mynote.database.Note
import com.example.mynote.viewmodel.NoteViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class NoteAdapter(val noteViewModel: NoteViewModel) : ListAdapter<Note, NoteAdapter.NoteHolder>(DIFF_CALLBACK), Filterable {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return newItem.id == oldItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title && oldItem.text == newItem.text && oldItem.pointed == newItem.pointed
            }

        }
    }

    private lateinit var listener: OnClickListener
    var filteredNotes: List<Note> = arrayListOf()
    var deleteMode = false
    var selectedItems : Int by Delegates.observable(0){ property, oldValue, newValue ->
        listener.select(newValue)
//        Log.e("items",currentList.toString())
    }



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteHolder {
        val itemView = LayoutInflater.from(p0.context)
            .inflate(R.layout.summary_show, p0, false)

        return NoteHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        holder.title.text = note.title
        holder.description.text = note.text
    }


/*    val setNotes : ( List<Note>) -> Unit = { notes ->
        this.notes = notes
        (filteredNotes as ArrayList).addAll(notes)
        notifyDataSetChanged()
    }*/

    fun getNoteAt(position: Int): Note {
        return  getItem(position)
    }

    inner class NoteHolder(itemView: View, listener: OnClickListener) : RecyclerView.ViewHolder(
        itemView
    ) {
        var title: TextView = itemView.findViewById(R.id.note_title)
        var description: TextView = itemView.findViewById(R.id.note_description)
        var tic: ImageView = itemView.findViewById(R.id.note_selected)

        init {
            itemView.setOnClickListener {
                if (!deleteMode) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(getItem(position))
                    }
                } else {
                    val note = getItem(adapterPosition)
                    if (!note.isSelected) {
                        selectItem(tic, itemView, note)
                    } else {
                        deselectItem(tic, itemView, note)
                    }
                }
            }
            itemView.setOnLongClickListener {
                if (!deleteMode) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val note = getItem(position)
                        selectItem(tic, itemView, note)
                        listener.onLongClick(note)
                    }
                    deleteMode = true
                }
                true
            }
        }
    }



    val selectItem: (image: ImageView, view: View, note: Note) -> Unit = { image, itemView, note, ->
        itemView.setBackgroundResource(R.color.selected)
        image.visibility = ImageView.VISIBLE
        if (!note.isSelected) {
            note.isSelected = true
            selectedItems += 1
            noteViewModel?.update(note)
        }
    }

    val deselectItem: (image: ImageView, view: View, note: Note) -> Unit =
        { image, itemView, note ->
            image.visibility = ImageView.INVISIBLE
            itemView.setBackgroundResource(R.color.white)
            if (note.isSelected) {
                note.isSelected = false
                selectedItems -= 1
                noteViewModel.update(note)
            }
        }

    interface OnClickListener {
        fun onClick(note: Note)
        fun onLongClick(note: Note)
        fun select(numberSelected: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        listener = onClickListener
    }

    override fun getFilter(): Filter = filtered


    private val filtered = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = ArrayList<Note>()
//            (filteredNotes as ArrayList).addAll(notes)
//            Log.e("",notes.toString())
            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(filteredNotes)
            } else {
                val stringPattern = p0.toString().trim().toLowerCase(Locale.ROOT)
                for (note in filteredNotes) {
                    if (note.text?.toLowerCase(Locale.ROOT)!!.contains(stringPattern)) {
                        filteredList.add(note)
                    }
                }
            }
            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            submitList(null)
            submitList(p1?.values as MutableList<Note>?)
//            currentList .clear()
//            currentList .addAll(p1?.values as List<Note>)
            notifyDataSetChanged()
        }


    }
}