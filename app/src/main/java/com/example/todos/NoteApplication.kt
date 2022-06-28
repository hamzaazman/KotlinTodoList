package com.example.todos

import android.app.Application
import com.example.todos.data.NoteDatabase

class NoteApplication : Application() {
    val database: NoteDatabase by lazy {
        NoteDatabase.getDatabase(this)
    }
}