package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.fesskiev.compose.ui.screens.*
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
                    composable("note_details/{noteUid}", arguments = listOf(navArgument("noteUid")
                    { type = NavType.IntType })) { backStackEntry ->
                        NoteDetailsScreen(
                            navController,
                            noteUid = backStackEntry.arguments?.getInt("noteUid") ?: -1,
                        )
                    }
                    composable("edit_note/{noteUid}", arguments = listOf(navArgument("noteUid")
                    { type = NavType.IntType })) { backStackEntry ->
                        EditNoteScreen(
                            navController,
                            noteUid = backStackEntry.arguments?.getInt("noteUid") ?: -1,
                        )
                    }
                }
            }
        }
    }
}
