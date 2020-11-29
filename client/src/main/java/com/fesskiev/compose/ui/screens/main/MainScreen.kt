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
import com.fesskiev.compose.presentation.MainScreenViewModel
import com.fesskiev.compose.ui.components.AppHamburgerToolbar
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import com.fesskiev.compose.ui.screens.notes.NotesList
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(navController: NavHostController, viewModel: MainScreenViewModel = getViewModel()) {
    val scaffoldState = rememberScaffoldState()
    viewModel.getNotes()
    Scaffold(scaffoldState = scaffoldState,
        topBar = {
            AppHamburgerToolbar(title = "NotesApp", hamburgerOnClick = {
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
                Image(asset = vectorResource(R.drawable.ic_plus_one))
            }
        },
        bodyContent = {
            val uiState = viewModel.liveData.observeAsState().value
            if (uiState != null) {
                when (uiState) {
                    MainUiState.Loading -> ProgressBar()
                    MainUiState.Empty -> {

                    }
                    is MainUiState.Data -> NotesList(uiState.notes, noteOnClick = {
                        navController.navigate("note_details")
                    }, deleteNoteOnClick = {}, editNoteOnClick = {})
                }
                if (uiState is MainUiState.Error) {
                    SnackBar(stringResource(uiState.errorResourceId))
                }
            } else {
                // draw something went wrong
            }
        })
}

@Composable
fun AppDrawer(settingsOnClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.preferredHeight(24.dp))
        TextButton(onClick = { settingsOnClick() }, modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                Image(asset = vectorResource(R.drawable.ic_home))
                Spacer(Modifier.preferredWidth(16.dp))
                Text(
                    text = "Settings",
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                    style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}