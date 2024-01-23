package com.example.to_do

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.databinding.HomeFragmentBinding
import com.example.to_do.mvvm.NoteRepository
import com.example.to_do.mvvm.NoteViewModel
import com.example.to_do.mvvm.NoteViewModelFactory
import com.example.to_do.room.Note
import com.example.to_do.room.NoteDatabase
import com.google.android.material.snackbar.Snackbar

class home_fragment : Fragment() {

    private lateinit var binding:HomeFragmentBinding
    private lateinit var viewModel :NoteViewModel
    private lateinit var noteAdapter:NoteAdapter
    val noteList= listOf<Note>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.home_fragment, container, false)

        binding = DataBindingUtil.inflate(inflater,R.layout.home_fragment,container,false)


        binding.addnotebtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_home_fragment_to_create_note)
        }
        val dao= NoteDatabase.invoke(requireContext()).noteDao()
        val repository = NoteRepository(noteDao = dao)
        val factory = NoteViewModelFactory(repo=repository)

        viewModel = ViewModelProvider(this, factory = factory).get(NoteViewModel::class.java)

        noteAdapter = NoteAdapter(noteList)
        binding.recyclerView.layoutManager = GridLayoutManager(context,2)
        viewModel.displayAllnotes.observe(
            viewLifecycleOwner, Observer {
                noteAdapter = NoteAdapter(it)
                binding.recyclerView.adapter = noteAdapter
            }
        )

        //swipe delete code

       ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
           override fun onMove(
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               target: RecyclerView.ViewHolder
           ): Boolean {
               return false
           }

           override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val note =noteAdapter.notes[viewHolder.adapterPosition]
               viewModel.deleteNote(note)
           }

       }).attachToRecyclerView(binding.recyclerView)


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.noteEvent.collect{ event->
                when(event){
                    is NoteViewModel.NoteEvent.ShowUndoDeleteMessage ->{
                        Snackbar.make(requireView(),"Note Deleted",Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.note)
                            }.show()
                    }
                }

            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                requireActivity().finish()
                requireActivity().moveTaskToBack(true)
            }

        })
    }


}