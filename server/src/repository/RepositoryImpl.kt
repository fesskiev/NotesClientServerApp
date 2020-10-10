package com.fesskiev.repository

import com.fesskiev.model.Note
import com.fesskiev.model.User
import java.util.*

class RepositoryImpl : Repository {

    private val notes = mutableListOf<Note>()
    private val users = mutableListOf<User>()

    override fun getNotes(uid: String): List<Note> = notes.filter { it.uid == uid }

    override fun addNote(uid: String, text: String): Note {
        val createTime = System.currentTimeMillis()
        val note = Note(uid, text, createTime)
        notes.add(note)
        return note
    }

    override fun updateNote(note: Note): Boolean {
        val index = notes.indexOf(note)
        if (index == -1) {
            return false
        }
        notes[index] = note
        return true
    }

    override fun deleteNote(note: Note): Boolean = notes.remove(note)

    override fun createUser(email: String, displayName: String, password: String): User {
        val uid = UUID.randomUUID().toString()
        val user = User(uid, email, displayName, password)
        users.add(user)
        return user
    }

    override fun getUserByUid(uid: String): User? = users.first { it.uid == uid }

    override fun getUserByEmail(email: String): User? = users.first { it.email == email }
}