package com.example.todos.ui

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todos.NoteApplication
import com.example.todos.R
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
    private var isLinearLayoutManager = false
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
            if (notes.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.emptyMessage.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyMessage.visibility = View.GONE
                notes.let {
                    noteAdapter.submitList(it)
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
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    private fun chooseItemLayout() {
        if (isLinearLayoutManager) {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    private fun setIcon(menuItem: MenuItem) {
        menuItem.icon =
            if (isLinearLayoutManager) {
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_menu)
            } else {
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_baseline_view)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gridMenu -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseItemLayout()
                setIcon(item)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}