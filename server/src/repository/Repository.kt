package com.fesskiev.repository

import com.fesskiev.model.Note
import com.fesskiev.model.User

interface Repository {

    fun getNotes(uid: String): List<Note>

    fun addNote(uid: String, text: String): Note

    fun updateNote(note: Note): Boolean

    fun deleteNote(note: Note): Boolean

    fun createUser(email: String, displayName: String, password: String): User

    fun getUserByUid(uid: String): User?

    fun getUserByEmail(email: String): User?
}