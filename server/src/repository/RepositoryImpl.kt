package com.fesskiev.repository

import com.fesskiev.db.*
import com.fesskiev.model.Note
import com.fesskiev.model.User

class RepositoryImpl : Repository {

    override suspend fun getNotes(uid: Int, page: Int): List<Note> = selectNotes(uid, page)

    override suspend fun addNote(uid: Int, title: String, description: String): Note? = insertNote(uid, title, description)

    override suspend fun editNote(note: Note): Boolean = updateNote(note)

    override suspend fun removeNote(note: Note): Boolean = deleteNote(note)

    override suspend fun createUser(email: String, displayName: String, password: String): User? = insertUser(email, displayName, password)

    override suspend fun getUserByUid(uid: Int): User? = selectUserById(uid)

    override suspend fun getUserByEmail(email: String): User? = selectUserByEmail(email)

    override suspend fun getNoteByUid(uid: Int): Note? = selectNoteById(uid)
}