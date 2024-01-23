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
import com.example.to_do.databinding.FragmentEditNoteBinding
import com.example.to_do.mvvm.NoteRepository
import com.example.to_do.mvvm.NoteViewModel
import com.example.to_do.mvvm.NoteViewModelFactory
import com.example.to_do.room.Note
import com.example.to_do.room.NoteDatabase

class edit_note : Fragment() {


    private lateinit var binding: FragmentEditNoteBinding
    lateinit var viewModel: NoteViewModel
    lateinit var title: String
    lateinit var desc: String
    lateinit var id: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit_note, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_note, container, false)
        val dao = NoteDatabase.invoke(requireContext()).noteDao()
        val repository = NoteRepository(dao)
        val factory = NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = requireArguments().getString("title").toString()
        desc = requireArguments().getString("desc").toString()
        id = requireArguments().getString("id").toString()
        binding.apply {
            edittitelEt.setText(title)
            editdescEt.setText(desc)
        }
        binding.edittitelEt.addTextChangedListener {
        if (it!!.count()>0){
            binding.titleEditTextLayout.error = null
            binding.saveNotebtn.visibility = View.VISIBLE
        }
        }
        binding.editdescEt.addTextChangedListener {
            if(it!!.count()>0){
                binding.descriptionEditTextLayout.error = null
                binding.saveNotebtn.visibility = View.VISIBLE
            }
        }

        binding.saveNotebtn.setOnClickListener {

            val newTitle = binding.edittitelEt.text.toString()
            val newDes = binding.editdescEt.text.toString()

            if(newTitle.isEmpty()){
                binding.titleEditTextLayout.error="Please Enter Valid Title"
            }
            if(newDes.isEmpty()){
                binding.descriptionEditTextLayout.error ="Please Enter Valid Desc"
            }
            if(newTitle.isNotEmpty()  && newDes.isNotEmpty()){
                viewModel.editNote(Note(id.toInt(),newTitle,newDes))
                Toast.makeText(context,"note updated",Toast.LENGTH_LONG).show()
                it.findNavController().navigate(R.id.action_edit_note_to_home_fragment)
            }

        }
        binding.closebtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_edit_note_to_home_fragment)
        }
    }
}
