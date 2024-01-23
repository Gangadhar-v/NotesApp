package com.example.to_do

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.to_do.databinding.NoteItemBinding
import com.example.to_do.room.Note

class NoteAdapter(val notes:List<Note>): Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: NoteItemBinding):ViewHolder(binding.root){

        fun bindView(note:Note){
            binding.titleTv.text = note.title
            binding.descTv.text = note.desc

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val layoutInflater=LayoutInflater.from(parent.context)
        val binding:NoteItemBinding =DataBindingUtil.inflate(layoutInflater,R.layout.note_item,parent,false)

        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        holder.bindView(notes[position])
        holder.itemView.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("title",notes[position].title)
            bundle.putString("desc",notes[position].desc)
            bundle.putString("id",notes[position].id.toString())

            it.findNavController().navigate(R.id.action_home_fragment_to_edit_note,bundle)
        }
    }

}