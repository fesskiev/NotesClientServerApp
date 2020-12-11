package com.fesskiev.compose.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.NotesViewModel
import com.fesskiev.compose.ui.components.AppHamburgerToolbar
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import com.fesskiev.compose.ui.screens.notes.NotesList
import com.fesskiev.compose.ui.screens.notes.NotesUiState
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavHostController, viewModel: NotesViewModel = getViewModel()) {
    val scaffoldState = rememberScaffoldState()
    viewModel.getNotes()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            AppHamburgerToolbar(title = stringResource(R.string.app_name), hamburgerOnClick = {
                scaffoldState.drawerState.open()
            })
        },
        drawerContent = {
            AppDrawer(settingsOnClick = {
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
                },
                backgroundColor = Color(0xFF272729)
            ) {
                Image(imageVector = vectorResource(R.drawable.ic_plus_one))
            }
        },
        bodyContent = {
            val state = viewModel.liveData.observeAsState().value
            if (state != null) {
                when (state) {
                    NotesUiState.Empty -> EmptyView()
                    is NotesUiState.Loading -> ProgressBar()
                    is NotesUiState.Data -> {
                        NotesList(
                            state.notes,
                            noteOnClick = { navController.navigate("note_details") },
                            deleteNoteOnClick = { viewModel.deleteNote(it) },
                            editNoteOnClick = { viewModel.editNote(it) })
                    }
                    else -> EmptyView()
                }
                if (state is NotesUiState.Error) {
                    SnackBar(stringResource(state.errorResourceId))
                }
            } else {
                EmptyView()
            }
        })
}

@Composable
fun EmptyView() {

}

@Composable
fun AppDrawer(settingsOnClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.preferredHeight(24.dp))
        TextButton(onClick = { settingsOnClick() }, modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                Image(imageVector = vectorResource(R.drawable.ic_settings))
                Spacer(Modifier.preferredWidth(16.dp))
                Text(
                    text = stringResource(R.string.settings),
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                    style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}