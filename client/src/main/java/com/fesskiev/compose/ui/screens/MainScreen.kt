package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.NotesListUiState
import com.fesskiev.compose.presentation.NotesListViewModel
import com.fesskiev.compose.ui.components.*
import com.fesskiev.model.Note
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavHostController, viewModel: NotesListViewModel = getViewModel()) {
    val scope = rememberCoroutineScope()
    val uiState = viewModel.stateFlow.collectAsState().value
    LaunchedEffect(0) {
        viewModel.getNotes(uiState.page)
    }
    AppScaffold(
        scope = scope,
        topBar = {
            AppHamburgerToolbar(title = stringResource(R.string.app_name), hamburgerOnClick = {
                scope.launch {
                    it.drawerState.open()
                }
            })
        }, drawerContent = {
            AppDrawer(onSettingsClick = {
                scope.launch {
                    it.drawerState.close()
                    navController.navigate("settings")
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_note")
                }
            ) {
                Image(painter = painterResource(R.drawable.ic_plus_one), contentDescription = "")
            }
        },
        content = {
            MainContent(
                uiState,
                onNoteClick = { navController.navigate("note_details/${it.noteUid}") },
                onNoteDeleteClick = { viewModel.deleteNote(it) },
                onNoteEditClick = { navController.navigate("edit_note/${it.noteUid}") },
                onLoadMoreItems = { viewModel.loadMoreNotes()})
        },
        errorResourceId = uiState.errorResourceId
    )
}

@Composable
fun MainContent(
    uiState: NotesListUiState,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    onLoadMoreItems: () -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
        uiState.notes != null -> {
            val notes = uiState.notes
            if (notes.isNotEmpty()) {
                NotesListScreen(
                    notes,
                    uiState.paging,
                    uiState.needLoadMore,
                    onNoteClick = onNoteClick,
                    onNoteDeleteClick = onNoteDeleteClick,
                    onNoteEditClick = onNoteEditClick,
                    onLoadMoreItems = onLoadMoreItems
                )
            } else {
                EmptyView(stringResource(R.string.empty_notes_list))
            }
        }
    }
}

