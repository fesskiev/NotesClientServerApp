package com.fesskiev.compose.state

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun provideUiStateSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences("UiStatePreferences", Context.MODE_PRIVATE)

class UiStateSaver(private val preferences: SharedPreferences) {

    var saveStateNotesListener: () -> NotesUiState? = { null }

    suspend fun saveStateEvent() {
        saveStateNotesListener()?.let { saveNotesState(it) }
    }

    private val NOTES_STATE_KEY = "notes_state"

    private suspend fun saveNotesState(notesUiState: NotesUiState) {
        withContext(Dispatchers.Default + NonCancellable) {
            val json = Json.encodeToString(notesUiState)
            with(preferences.edit()) {
                putString(NOTES_STATE_KEY, json)
                commit()
            }
        }
    }

    suspend fun restoreNotesState(): NotesUiState? =
        withContext(Dispatchers.Default) {
            val json = preferences.getString(NOTES_STATE_KEY, "")
            if (json != null && json.isNotEmpty()) {
                return@withContext Json.decodeFromString<NotesUiState>(json)
            }
            return@withContext null
        }

    suspend fun clearUiState() {
        withContext(Dispatchers.Default) {
            with(preferences.edit()) {
                clear()
                commit()
            }
        }
    }
}