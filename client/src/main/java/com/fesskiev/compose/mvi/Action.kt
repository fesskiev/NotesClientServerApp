package com.fesskiev.compose.mvi

import com.fesskiev.model.Note

sealed class Action {
    object Idle : Action()
    data class DeleteNote(val note: Note) : Action()
    data class AddNote(val note: Note) : Action()
    data class EditNote(val note: Note) : Action()
}