package com.fesskiev.compose.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppScaffold(
    scope: CoroutineScope = rememberCoroutineScope(),
    topBar: @Composable (ScaffoldState) -> Unit = { },
    drawerContent: @Composable (ScaffoldState) -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { },
    drawerGesturesEnabled: Boolean = true,
    @StringRes errorResourceId: Int? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            topBar(scaffoldState)
        },
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    modifier = Modifier.border(2.dp, MaterialTheme.colors.background),
                    snackbarData = data
                )
            }
        },
        drawerContent = {
            drawerContent(scaffoldState)
        },
        drawerGesturesEnabled = drawerGesturesEnabled,
        content = content,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = floatingActionButton
    )
    if (errorResourceId != null) {
        val message = stringResource(errorResourceId)
        LaunchedEffect(errorResourceId) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = message)
            }
        }
    }
}