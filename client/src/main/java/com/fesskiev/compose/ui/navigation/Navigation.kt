package com.fesskiev.compose.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fesskiev.compose.ui.screens.AuthScreen
import com.fesskiev.compose.ui.screens.MainScreen

fun NavGraphBuilder.authGraph(navController: NavHostController, onCloseAppClick: () -> Unit) {
    navigation(
        route = AuthGraph.route,
        startDestination = AuthGraph.AuthScreen.route,
    ) {
        composable(AuthGraph.AuthScreen.route) {
            AuthScreen(
                onCloseAppClick = onCloseAppClick,
                authSuccess = { navController.navigate(MainGraph.route) }
            )
        }
    }
}

fun NavGraphBuilder.mainGraph(onCloseAppClick: () -> Unit) {
    navigation(
        route = MainGraph.route,
        startDestination = MainGraph.MainScreen.route,
    ) {
        composable(MainGraph.MainScreen.route) {
            MainScreen(onCloseAppClick = onCloseAppClick)
        }
    }
}