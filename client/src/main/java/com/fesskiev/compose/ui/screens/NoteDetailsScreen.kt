package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.NoteDetailsUiState
import com.fesskiev.compose.presentation.NotesDetailsViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.AppScaffold
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.model.Note
import dev.chrisbanes.accompanist.coil.CoilImage
import org.koin.androidx.compose.getViewModel

@Composable
fun NoteDetailsScreen(
    navController: NavHostController,
    viewModel: NotesDetailsViewModel = getViewModel(),
    noteUid: Int
) {
    val uiState = viewModel.stateFlow.collectAsState().value
    onActive {
        viewModel.getNoteByUid(noteUid)
    }
    AppScaffold(
        topBar = {
            AppBackToolbar(stringResource(R.string.note_details)) {
                navController.popBackStack()
            }
        },
        bodyContent = {
            NoteDetailsContent(uiState)
        },
        errorResourceId = uiState.errorResourceId
    )
}

@Composable
fun NoteDetailsContent(uiState: NoteDetailsUiState) {
    when {
        uiState.loading -> ProgressBar()
        uiState.note != null -> NoteDetails(uiState.note)
    }
}

@Composable
fun NoteDetails(note: Note) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.preferredHeight(25.dp))
        Text(text = note.title, style = MaterialTheme.typography.h5)
        Spacer(Modifier.preferredHeight(20.dp))
        Text(text = note.description, style = MaterialTheme.typography.body1)
        Spacer(Modifier.preferredHeight(25.dp))
        note.pictureUrl?.let {
            CoilImage(
                data = it,
                fadeIn = true,
                contentScale = ContentScale.Crop,
                loading = { ProgressBar() },
                modifier = Modifier.preferredSize(300.dp)
            )
        }
    }
}
