package com.example.todos.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todos.NoteApplication
import com.example.todos.R
import com.example.todos.databinding.FragmentEditBinding
import com.example.todos.model.Note
import com.example.todos.viewModel.NoteViewModel

class EditFragment : Fragment() {
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModel.NoteViewModelFactory(
            (activity?.application as NoteApplication).database.noteDao()
        )
    }
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    lateinit var note: Note
    private val args: EditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.id

        viewModel.getNoteById(id).observe(viewLifecycleOwner) { selectedNote ->
            note = selectedNote
            bind(note)
        }

    }

    private fun bind(note: Note) {
        binding.apply {
            etTitle.setText(note.title, TextView.BufferType.SPANNABLE)
            etDescription.setText(note.description, TextView.BufferType.SPANNABLE)
        }
    }

    private fun updateNote() {
        viewModel.updateNote(
            this.args.id,
            this.binding.etTitle.text.toString(),
            this.binding.etDescription.text.toString()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_update, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.updateNote -> {
                updateNote()
                findNavController().popBackStack()
                true
            }
            R.id.deleteNote -> {
                deleteDialog(args.id)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun deleteDialog(id: Long) {
        AlertDialog.Builder(requireContext())
            .setMessage("Do you want to delete note?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                viewModel.deleteNote(id)
                findNavController().popBackStack()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.cancel()
            }).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}