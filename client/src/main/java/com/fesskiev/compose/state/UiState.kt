package com.fesskiev.compose.state

import androidx.annotation.StringRes
import com.fesskiev.compose.data.PagingSource
import com.fesskiev.compose.model.Note
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.io.File
import kotlin.random.Random

interface UiState

@Serializable
data class AuthUiState(
    val authSuccess: Boolean = false,
    val loading: Boolean = false,
    val displayName: String = "",
    val email: String = "test100@i.ua",
    val password: String = "123456",
    val isLoginFormShow: Boolean = true,
    val authUserInputState: AuthUserInputState = AuthUserInputState(),
    val error: ErrorState? = null
) : UiState

@Serializable
data class AuthUserInputState(
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false
) : UiState

@Serializable
data class SettingsUiState(
    val loading: Boolean = false,
    val isLogout: Boolean = false,
    val themeMode: String = "",
    val error: ErrorState? = null
) : UiState

@Serializable
data class EditNoteUiState(
    val editNoteSuccess: Boolean = false,
    val loading: Boolean = false,
    val editNoteUserInputState: NoteUserInputState = NoteUserInputState(),
    val title: String = "",
    val description: String = "",
    val error: ErrorState? = null
) : UiState

@Serializable
data class AddNoteUiState(
    val AddNoteSuccess: Boolean = false,
    val loading: Boolean = false,
    @Contextual
    val imageFile: File? = null,
    val addNoteUserInputState: NoteUserInputState = NoteUserInputState(),
    val title: String = "",
    val description: String = "",
    val error: ErrorState? = null
) : UiState

@Serializable
data class NoteUserInputState(
    val isEmptyTitleError: Boolean = false,
    val isEmptyDescriptionError: Boolean = false,
)

@Serializable
data class SearchNotesUiState(
    val notes: List<Note>? = null,
    val query: String = "",
    val loading: Boolean = false,
    val error: ErrorState? = null
) : UiState {
    override fun toString(): String {
        return "list: size=${notes?.size}, query=$query loading=$loading, error=$error)"
    }
}

@Serializable
data class NotesListUiState(
    val notes: List<Note>? = null,
    val loading: Boolean = false,
    val refresh: Boolean = false,
    val paging: PagingState = PagingState(),
    val selectedNote: Note? = null,
    val error: ErrorState? = null
) : UiState {

    override fun toString(): String {
        val listState =
            "list: size=${notes?.size}, loading=$loading, refresh=$refresh, selectedNote=$selectedNote)"
        val pagingState =
            "paging: next page=${paging.page}, more=${paging.loadMore}, endReached=${paging.endOfPaginationReached}, source=${paging.pagingSource} "
        val errorState = "error: id=${error?.errorResourceId}"
        return listState + "\n" + pagingState + "\n" + errorState
    }
}

@Serializable
data class PagingState(
    val page: Int = -1,
    val loadMore: Boolean = false,
    val endOfPaginationReached: Boolean = false,
    val pagingSource: PagingSource = PagingSource.LOCAL
) : UiState

@Serializable
data class ErrorState(
    val id: Int = Random.nextInt(),
    @StringRes
    val errorResourceId: Int? = null
) : UiState

@Serializable
data class NotesUiState(
    val notesListUiState: NotesListUiState = NotesListUiState(),
    val searchNotesUiState: SearchNotesUiState = SearchNotesUiState(),
    val addNoteUiState: AddNoteUiState = AddNoteUiState(),
    val editNoteUiState: EditNoteUiState = EditNoteUiState()
) : UiState