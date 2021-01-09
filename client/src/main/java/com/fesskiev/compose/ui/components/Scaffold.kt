package com.fesskiev.compose.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppScaffold(
    topBar: @Composable (ScaffoldState) -> Unit = { },
    drawerContent: @Composable (ScaffoldState) -> Unit = { },
    bodyContent: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit = { },
    @StringRes errorResourceId: Int? = null
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
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
        bodyContent = bodyContent,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = floatingActionButton
    )
    if (errorResourceId != null) {
        val message = stringResource(errorResourceId)
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(message = message)
        }
    }
}