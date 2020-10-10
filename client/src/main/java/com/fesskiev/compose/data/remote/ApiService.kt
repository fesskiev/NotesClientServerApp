package com.fesskiev.compose.data.remote

import com.fesskiev.HTTPParameters.DISPLAY_NAME
import com.fesskiev.HTTPParameters.EMAIL
import com.fesskiev.HTTPParameters.NOTE_TEXT
import com.fesskiev.HTTPParameters.PASSWORD
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.Routes.UPDATE_NOTE
import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note
import retrofit2.http.*

interface ApiService {

    @GET(GET_NOTES)
    suspend fun getNotes(): List<Note>

    @FormUrlEncoded
    @POST(ADD_NOTE)
    suspend fun addNote(@Field(NOTE_TEXT) text: String): Note

    @PUT(UPDATE_NOTE)
    suspend fun updateNote(@Body note: Note): Boolean

    @DELETE(DELETE_NOTE)
    suspend fun deleteNote(@Body note: Note): Boolean

    @FormUrlEncoded
    @POST(REGISTRATION)
    suspend fun registration(@Field(EMAIL) email: String, @Field(DISPLAY_NAME) displayName: String, @Field(PASSWORD) password: String): JWTAuth

    @POST(LOGIN)
    suspend fun login(email: String, password: String): JWTAuth

    @POST(LOGOUT)
    suspend fun logout()
}