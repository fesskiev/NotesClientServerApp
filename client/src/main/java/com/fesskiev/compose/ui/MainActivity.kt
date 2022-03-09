package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fesskiev.compose.state.UiStateSaver
import com.fesskiev.compose.ui.navigation.AuthGraph
import com.fesskiev.compose.ui.navigation.authGraph
import com.fesskiev.compose.ui.navigation.mainGraph
import com.fesskiev.compose.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val uiStateSaver: UiStateSaver by inject()

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleScope.launch {
            uiStateSaver.saveStateEvent()
        }
    }
}
