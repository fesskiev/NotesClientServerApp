package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fesskiev.compose.ui.navigation.AuthGraph
import com.fesskiev.compose.ui.navigation.authGraph
import com.fesskiev.compose.ui.navigation.mainGraph
import com.fesskiev.compose.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {

    private val closeAppClick: () -> Unit = { finish() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = AuthGraph.route) {
                    authGraph(
                        navController,
                        onCloseAppClick = closeAppClick
                    )
                    mainGraph(
                        onCloseAppClick = closeAppClick,
                        onLogoutClick = {
                            navController.navigate(AuthGraph.route)
                        }
                    )
                }
            }
        }
    }
}
