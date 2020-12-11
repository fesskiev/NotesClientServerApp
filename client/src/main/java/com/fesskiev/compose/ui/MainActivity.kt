package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.auth.AuthScreen
import com.fesskiev.compose.ui.screens.main.MainScreen
import com.fesskiev.compose.ui.screens.notes.add.AddNoteScreen
import com.fesskiev.compose.ui.screens.notes.details.NoteDetailsScreen
import com.fesskiev.compose.ui.screens.settings.SettingsScreen
import com.fesskiev.compose.ui.utils.AppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val repository by inject<Repository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "auth") {
                    composable("auth") { AuthScreen(navController) }
                    composable("main") { MainScreen(navController) }
                    composable("settings") { SettingsScreen(navController) }
                    composable("add_note") { AddNoteScreen(navController) }
                    composable("note_details") { NoteDetailsScreen(navController) }
                }
            }
        }

//        registrationFlow()
    }

    private fun registrationFlow() {
        GlobalScope.launch {
            try {
                repository.registration("test1@i.ua", "Max", "123456")
                repeat(10) {
                    repository.addNote("title: $it", "desc: $it", "url: $it")
                }
                val notes = repository.getNotes()
                repository.deleteNote(notes.first())

                repository.getNotes()
                repository.editNote(notes.first().copy(title = "copy title", description = "copy desc!"))

                repository.logout()
                repository.getNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
