package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.NotesListUiState
import com.fesskiev.compose.presentation.NotesListViewModel
import com.fesskiev.compose.ui.components.*
import com.fesskiev.model.Note
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavHostController, viewModel: NotesListViewModel = getViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val uiState = viewModel.stateFlow.collectAsState().value
    onActive {
        viewModel.getNotes()
    }
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            AppHamburgerToolbar(title = stringResource(R.string.app_name), hamburgerOnClick = {
                scaffoldState.drawerState.open()
            })
        },
        drawerContent = {
            AppDrawer(onSettingsClick = {
                scaffoldState.drawerState.close(onClosed = {
                    navController.navigate("settings")
                })
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_note")
                }
            ) {
                Image(imageVector = vectorResource(R.drawable.ic_plus_one))
            }
        },
        bodyContent = {
            MainContent(
                uiState,
                onNoteClick = { navController.navigate("note_details/${it.noteUid}") },
                onNoteDeleteClick = { viewModel.deleteNote(it) },
                onNoteEditClick = { navController.navigate("edit_note/${it.noteUid}") })
        })
}

@Composable
fun MainContent(
    uiState: NotesListUiState,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
        uiState.notes != null -> {
            val notes = uiState.notes
            if (notes.isNotEmpty()) {
                NotesListScreen(
                    notes,
                    onNoteClick = onNoteClick,
                    onNoteDeleteClick = onNoteDeleteClick,
                    onNoteEditClick = onNoteEditClick
                )
            } else {
                EmptyView(stringResource(R.string.empty_notes_list))
            }
        }
    }
    uiState.errorResourceId?.let {
        SnackBar(stringResource(it))
    }
}

