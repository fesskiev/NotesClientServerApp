package com.fesskiev.compose.data

import com.fesskiev.compose.data.local.DatabaseSource
import com.fesskiev.compose.data.remote.NetworkSource
import com.fesskiev.compose.model.JWTAuth
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.model.User
import java.io.File

class RepositoryImpl(
    private val networkSource: NetworkSource,
    private val databaseSource: DatabaseSource
) : Repository {

    private var user: User? = null

    override suspend fun login(email: String, password: String): JWTAuth {
        val jwtAuth = networkSource.login(email, password)
        val usr = networkSource.getUser()
        user = usr
        return jwtAuth
    }

    override suspend fun registration(
        email: String,
        displayName: String,
        password: String
    ): JWTAuth {
        val jwtAuth = networkSource.registration(email, displayName, password)
        val usr = networkSource.getUser()
        user = usr
        databaseSource.saveUser(usr)
        return jwtAuth
    }

    override suspend fun pagingNotes(page: Int): PagingResult<Note> {
        if (page < 1) {
            throw IllegalArgumentException()
        }
        val usr = user ?: throw IllegalStateException()

        val dbNotes = databaseSource.pagingNotes(uid = usr.uid, page = page)
        if (dbNotes.isEmpty()) {
            val remoteNotes = networkSource.pagingNotes(page)
            if (remoteNotes.isNotEmpty()) {
                databaseSource.saveNotes(remoteNotes)
                return PagingResult(list = remoteNotes, pagingSource = PagingSource.REMOTE)
            }
        }
        return PagingResult(list = dbNotes, pagingSource = PagingSource.LOCAL)
    }

    override suspend fun addNote(title: String, description: String, file: File?): Note {
        var note = networkSource.addNote(title, description)
        file?.let {
            note = networkSource.addImage(note, it)
        }
        databaseSource.saveNotes(listOf(note))
        return note
    }

    override suspend fun editNote(note: Note): Boolean {
        val result = networkSource.editNote(note)
        if (result) {
            databaseSource.editNote(note)
        }
        return result
    }

    override suspend fun deleteNote(note: Note): Boolean {
        val result = networkSource.deleteNote(note)
        if (result) {
            databaseSource.deleteNote(note)
        }
        return result
    }

    override suspend fun deleteAllNotes() {
        databaseSource.deleteAllNotes()
    }

    override suspend fun logout() {
        user = null
        networkSource.logout()
    }
}