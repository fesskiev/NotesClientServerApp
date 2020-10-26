package com.fesskiev.repository

import com.fesskiev.db.*
import com.fesskiev.model.Note
import com.fesskiev.model.User

class RepositoryImpl : Repository {

    override suspend fun getNotes(uid: Int): List<Note> = selectNotes(uid)

    override suspend fun addNote(uid: Int, text: String): Note? = insertNote(uid, text)

    override suspend fun editNote(note: Note): Boolean = updateNote(note)

    override suspend fun removeNote(note: Note): Boolean = deleteNote(note)

    override suspend fun createUser(email: String, displayName: String, password: String): User? = insertUser(email, displayName, password)

    override suspend fun getUserByUid(uid: Int): User? = selectUserById(uid)

    override suspend fun getUserByEmail(email: String): User? = selectUserByEmail(email)
}