package com.example.todos.viewModel

import androidx.lifecycle.*
import com.example.todos.data.NoteDao
import com.example.todos.model.Note
import com.example.todos.model.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    fun isNoteEmpty(title: String, description: String?): Boolean {
        if (title.isBlank()) {
            return false
        }
        return true
    }

    fun fromPriority(priority: String): Priority {
        return when (priority) {
            "HIGH" -> Priority.HIGH
            "MEDIUM" -> Priority.MEDIUM
            "LOW" -> Priority.LOW
            else -> {
                Priority.LOW
            }
        }
    }

    fun addNewNote(note: Note) {
        //val newNote =
        //  Note(noteId = id, title = title, description = description, priority = priority)

        insertNote(note)
    }

    private fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }


    fun getNoteById(id: Long): LiveData<Note> {
        return noteDao.getItem(id).asLiveData()
    }

    fun updateNote(
        id: Long, title: String, description: String, priority: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.updateNote(id, title, description, priority)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(id)
        }
    }

    class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return NoteViewModel(noteDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}