package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fesskiev.compose.ui.screens.auth.AuthScreen
import com.fesskiev.compose.ui.screens.main.MainScreen
import com.fesskiev.compose.ui.screens.notes.add.AddNoteScreen
import com.fesskiev.compose.ui.screens.notes.details.NoteDetailsScreen
import com.fesskiev.compose.ui.screens.settings.SettingsScreen
import com.fesskiev.compose.ui.utils.AppTheme

class MainActivity : AppCompatActivity() {

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
    }
}