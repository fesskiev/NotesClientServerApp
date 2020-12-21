package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.fesskiev.compose.presentation.NotesListViewModel
import com.fesskiev.compose.ui.components.*
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavHostController, viewModel: NotesListViewModel = getViewModel()) {
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
            val uiState = viewModel.stateFlow.collectAsState().value
            when {
                uiState.loading -> ProgressBar()
                uiState.notes != null -> {
                    val notes = uiState.notes
                    if (notes.isNotEmpty()) {
                        NotesListScreen(
                            notes,
                            noteOnClick = { navController.navigate("note_details/${it.noteUid}") },
                            deleteNoteOnClick = { viewModel.deleteNote(it) },
                            editNoteOnClick = {
                                navController.navigate("edit_note/${it.noteUid}")
                            })
                    } else {
                        EmptyView(stringResource(R.string.empty_notes_list))
                    }
                }
            }
            uiState.errorResourceId?.let {
                SnackBar(stringResource(it))
            }
        })
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