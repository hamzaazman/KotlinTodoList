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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todos.NoteApplication
import com.example.todos.R
import com.example.todos.databinding.FragmentAddBinding
import com.example.todos.model.Note
import com.example.todos.util.setCustomBackground
import com.example.todos.viewModel.NoteViewModel
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveFab.setOnClickListener {
            addNote()
        }

        resources.getStringArray(R.array.priority_list).forEach {
            println(it)

        }

        binding.spinnerPriority.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = "LOW"),
                    IconSpinnerItem(text = "MEDIUM"),
                    IconSpinnerItem(text = "HIGH")
                )
            )
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(context)

            setOnSpinnerItemSelectedListener { oldIndex, oldItem: IconSpinnerItem?, newIndex, newItem: IconSpinnerItem? ->
                setCustomBackground(requireContext())
            }

            setOnClickListener {
                showOrDismiss()
                setCustomBackground(requireContext())
            }

            lifecycleOwner = this@AddFragment
        }
    }

    private fun isNoteValid(): Boolean {
        return viewModel.isNoteEmpty(
            binding.etTitle.text.toString(), binding.etDescription.text.toString()
        )
    }

    private fun addNote() {
        if (isNoteValid()) {
            with(binding) {
                val newNote = Note(
                    noteId = 0,
                    title = etTitle.text.toString(),
                    description = etDescription.text.toString(),
                    priority = spinnerPriority.text.toString()
                )
                viewModel.addNewNote(newNote)
            }

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