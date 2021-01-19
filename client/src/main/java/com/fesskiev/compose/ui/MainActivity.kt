package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.*
import com.fesskiev.compose.ui.theme.AppTheme
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
                    composable(
                        "note_details/{noteUid}", arguments = listOf(navArgument("noteUid")
                        { type = NavType.IntType })
                    ) { backStackEntry ->
                        NoteDetailsScreen(
                            navController,
                            noteUid = backStackEntry.arguments?.getInt("noteUid") ?: -1,
                        )
                    }
                    composable(
                        "edit_note/{noteUid}", arguments = listOf(navArgument("noteUid")
                        { type = NavType.IntType })
                    ) { backStackEntry ->
                        EditNoteScreen(
                            navController,
                            noteUid = backStackEntry.arguments?.getInt("noteUid") ?: -1,
                        )
                    }
                }
            }
        }
//        registrationFlow()
    }

    private fun registrationFlow() {
        GlobalScope.launch {
            try {
                repository.registration("test10@i.ua", "Max", "123456")
                repeat(100) {
                    repository.addNote("title: $it", "desc: $it", "url: $it")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
