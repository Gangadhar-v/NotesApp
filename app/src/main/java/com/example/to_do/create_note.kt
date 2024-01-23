package com.example.to_do

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.to_do.databinding.FragmentCreateNoteBinding
import com.example.to_do.mvvm.NoteRepository
import com.example.to_do.mvvm.NoteViewModel
import com.example.to_do.mvvm.NoteViewModelFactory
import com.example.to_do.room.Note
import com.example.to_do.room.NoteDatabase

class create_note : Fragment() {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_create_note, container, false)

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_note,container,false)
        val dao= NoteDatabase.invoke(requireContext()).noteDao()
        val repository = NoteRepository(noteDao = dao)
        val factory = NoteViewModelFactory(repo=repository)

        viewModel = ViewModelProvider(this, factory = factory).get(NoteViewModel::class.java)
        binding.viewModel = viewModel
        //to observe the live data
        binding.lifecycleOwner = this
        binding.titleEt.addTextChangedListener {
            if (it!!.count()>0){
                binding.titleEditTextLayout.error = null
            }
        }
        binding.descEt.addTextChangedListener {
            if(it!!.count()>0){
                binding.descriptionEditTextLayout.error = null
            }
        }
        binding.saveNotebtn.setOnClickListener{

            val title=binding.titleEt.text.toString()
            val desc=binding.descEt.text.toString()
            if(title.isEmpty()){
                binding.titleEditTextLayout.error="Please Enter Valid Title"
            }
            if(desc.isEmpty()){
                binding.descriptionEditTextLayout.error ="Please Enter Valid Desc"
            }
            if(title.isNotEmpty()  && desc.isNotEmpty()){
                viewModel.addNote()
                it.findNavController().navigate(R.id.action_create_note_to_home_fragment)
                Toast.makeText(context, "note saved", Toast.LENGTH_SHORT).show()

            }

        }
        binding.closebtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_create_note_to_home_fragment)
        }

        return binding.root
    }
}