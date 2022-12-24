package com.example.todos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todos.NoteApplication
import com.example.todos.adapters.NoteAdapter
import com.example.todos.databinding.FragmentListBinding
import com.example.todos.viewModel.NoteViewModel

class ListFragment : Fragment() {
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModel.NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupRV()
        observeNotes()

        binding.fabAdd.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddFragment()
            findNavController().navigate(action)
        }

    }

    private fun observeNotes() {
        viewModel.allNotes.observe(this.viewLifecycleOwner) { notes ->
            with(binding) {
                if (notes.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    lottieEmptyList.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    lottieEmptyList.visibility = View.GONE
                    notes.let {
                        noteAdapter.submitList(it)
                    }
                }
            }

        }
    }

    private fun setupRV() {
        noteAdapter = NoteAdapter {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(it.noteId)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = noteAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}