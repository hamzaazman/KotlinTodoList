package com.example.todos.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todos.NoteApplication
import com.example.todos.databinding.FragmentAddBinding
import com.example.todos.viewModel.NoteViewModel


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModel.NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveFab.setOnClickListener {
            addNote()
        }
    }

    private fun isNoteValid(): Boolean {
        return viewModel.isNoteEmpty(
            binding.etTitle.text.toString(), binding.etDescription.text.toString()
        )
    }

    private fun addNote() {
        if (isNoteValid()) {
            viewModel.addNewNote(
                id = 0, binding.etTitle.text.toString(), binding.etDescription.text.toString()
            )

            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        super.onDestroyView()
        _binding = null
    }

}