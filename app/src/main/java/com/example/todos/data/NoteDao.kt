package com.example.todos.data

import androidx.room.*
import com.example.todos.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("SELECT * FROM note_table ORDER BY noteId DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * from note_table WHERE noteId = :id")
    fun getItem(id: Long): Flow<Note>

    @Query("UPDATE note_table SET title= :title, description = :description WHERE noteId LIKE :id")
    fun updateNote(id: Long, title: String, description: String)

    @Query("DELETE FROM note_table WHERE noteId = :id")
    fun deleteNote(id: Long)
}