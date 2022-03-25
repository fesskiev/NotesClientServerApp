package com.fesskiev.compose.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.state.ErrorState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppScaffold(
    scope: CoroutineScope = rememberCoroutineScope(),
    onBackPressed: () -> Unit = { },
    topBar: @Composable (ScaffoldState) -> Unit = { },
    bottomBar: @Composable () -> Unit = {},
    drawerContent: @Composable (ScaffoldState) -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { },
    drawerGesturesEnabled: Boolean = true,
    error: ErrorState? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    BackHandler {
        if (scaffoldState.drawerState.isOpen) {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        } else {
            onBackPressed()
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { topBar(scaffoldState) },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.border(2.dp, MaterialTheme.colors.background),
                    snackbarData = data
                )
            }
        },
        drawerShape = RectangleShape,
        drawerContent = {
            drawerContent(scaffoldState)
        },
        drawerGesturesEnabled = drawerGesturesEnabled,
        content = content,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = floatingActionButton
    )
    if (error != null) {
        val message = error.errorResourceId?.let { stringResource(it) } ?: ""
        LaunchedEffect(error.id) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = message)
            }
        }
    }
}