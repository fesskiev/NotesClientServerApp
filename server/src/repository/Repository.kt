package com.fesskiev.repository

import com.fesskiev.model.Note
import com.fesskiev.model.User

interface Repository {

    suspend fun getNotes(uid: Int, page: Int): List<Note>

    suspend fun addNote(uid: Int, title: String, description: String): Note?

    suspend fun editNote(note: Note): Boolean

    suspend fun removeNote(note: Note): Boolean

    suspend fun createUser(email: String, displayName: String, password: String): User?

    suspend fun getUserByUid(uid: Int): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun getNoteByUid(uid: Int): Note?
}