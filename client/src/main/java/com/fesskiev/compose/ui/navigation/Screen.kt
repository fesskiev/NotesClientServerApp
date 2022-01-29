package com.fesskiev.compose.ui.navigation

import androidx.annotation.StringRes
import com.fesskiev.compose.R

const val AUTH = "auth"
const val AUTH_SCREEN = "auth.screen"

const val MAIN = "main"
const val MAIN_SCREEN = "main.screen"
const val NOTES_LIST_SCREEN = "notes_list.screen"
const val ADD_NOTE_SCREEN = "add_note.screen"
const val EDIT_NOTE_SCREEN = "edit_note.screen"
const val NOTE_DETAILS_SCREEN = "note_details.screen"
const val SETTINGS_SCREEN = "settings.screen"
const val SETTINGS_THEME_DIALOG = "settings.dialog.theme"

object AuthGraph : Screen(AUTH, R.string.settings) {

    object AuthScreen : Screen(AUTH_SCREEN, R.string.settings)
}

object MainGraph : Screen(MAIN, R.string.app_name) {

    object MainScreen : Screen(MAIN_SCREEN, R.string.app_name)

    object NotesListScreen : Screen(NOTES_LIST_SCREEN, R.string.app_name)

    object AddNoteScreen : Screen(ADD_NOTE_SCREEN, R.string.add_note)

    object EditNoteScreen : Screen(EDIT_NOTE_SCREEN, R.string.edit_note)

    object NoteDetailsScreen : Screen(NOTE_DETAILS_SCREEN, R.string.note_details)

    object SettingsScreen : Screen(SETTINGS_SCREEN, R.string.settings)

    object SettingsThemeDialog : Screen(SETTINGS_THEME_DIALOG, R.string.settings)
}

sealed class Screen(val route: String, @StringRes val resourceId: Int)

fun String.currentScreenByRoute(): Screen? =
    when (this) {
        AUTH_SCREEN -> AuthGraph.AuthScreen
        MAIN_SCREEN -> MainGraph.MainScreen
        NOTES_LIST_SCREEN -> MainGraph.NotesListScreen
        ADD_NOTE_SCREEN -> MainGraph.AddNoteScreen
        EDIT_NOTE_SCREEN -> MainGraph.EditNoteScreen
        NOTE_DETAILS_SCREEN -> MainGraph.NoteDetailsScreen
        SETTINGS_SCREEN -> MainGraph.SettingsScreen
        SETTINGS_THEME_DIALOG -> MainGraph.SettingsThemeDialog
        else -> null
    }
