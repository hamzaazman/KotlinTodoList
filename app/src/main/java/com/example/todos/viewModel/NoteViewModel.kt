package com.example.todos.viewModel

import androidx.lifecycle.*
import com.example.todos.data.NoteDao
import com.example.todos.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getNotes().asLiveData()

    fun isNoteEmpty(title: String, description: String): Boolean {
        if (title.isBlank() || description.isBlank()) {
            return false
        }
        return true
    }

    fun addNewNote(id: Long = 0, title: String, description: String) {
        val newNote = Note(id, title, description)
        insertNote(newNote)
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
        id: Long,
        title: String,
        description: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.updateNote(id, title, description)
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
                @Suppress("UNCHECKED_CAST")
                return NoteViewModel(noteDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}