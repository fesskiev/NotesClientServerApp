package com.fesskiev.compose.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesskiev.Database.CLIENT_URL
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.MainScreenViewModel
import com.fesskiev.compose.ui.screens.auth.AuthScreen
import com.fesskiev.compose.ui.screens.main.MainScreen
import com.fesskiev.compose.ui.screens.settings.SettingsScreen
import com.fesskiev.compose.ui.utils.AppTheme
import com.fesskiev.db.DatabaseFactory
import com.fesskiev.db.insertNote
import com.fesskiev.db.selectNotes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * https://developer.android.com/jetpack/compose/navigation
 */
class MainActivity : AppCompatActivity() {

    private val repository by inject<Repository>()
    private val navigationViewModel by viewModel<MainScreenViewModel>(state = { Bundle() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "auth") {
                    composable("auth") { AuthScreen(navController) }
                    composable("main") { MainScreen(navController) }
                    composable("settings") { SettingsScreen(navController) }
                }
            }
        }
//        navigationViewModel.notesLiveData.observe(this, { notes ->
//            Log.wtf("test", "notes: $notes")
//        })
//        navigationViewModel.getNotes()

//        createDatabase()
//        registrationFlow()
//        loginFlow()
    }

    private fun createDatabase() {
        DatabaseFactory.init(CLIENT_URL)
        GlobalScope.launch {
            insertNote(0, "text")
            val note = selectNotes(0)
            Log.w("test", "note: $note")
        }
    }

    private fun loginFlow() {
        GlobalScope.launch {
            try {
                repository.login("fesskiev@gmail.com", "123456")
                repository.getNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun registrationFlow() {
        GlobalScope.launch {
            try {
                repository.registration("fesskiev@gmail.com", "Max", "123456")
                repeat(10) {
                    repository.addNote("text test")
                }
                val notes = repository.getNotes()
                repository.deleteNote(notes.first())

                repository.getNotes()
                repository.editNote(notes.first().copy(text = "copy text"))

                repository.logout()
                repository.getNotes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}