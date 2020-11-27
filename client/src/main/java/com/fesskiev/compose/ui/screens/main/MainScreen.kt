package com.fesskiev.compose.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.fesskiev.model.Note
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
                scaffoldState.drawerState.close()
                navController.navigate("settings")
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Icon(Icons.Filled.Favorite)
            }
        },
        bodyContent = { padding ->
            when (val uiState = viewModel.liveData.observeAsState().value) {
                MainUiState.Loading -> ProgressBar()
                is MainUiState.Data -> NotesList(padding, navController, uiState.notes)
            }
        })
}

@Composable
fun NotesList(padding: PaddingValues, navController: NavHostController, notes: List<Note>) {
    ScrollableColumn {
        Column(Modifier.padding(padding)) {

        }
    }
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
                    style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)
                )
            }
        }
    }
}