package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.BuildConfig
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.NoteDetailsUiState
import com.fesskiev.compose.presentation.NoteDetailsViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.AppScaffold
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.model.Note
import dev.chrisbanes.accompanist.coil.CoilImage
import org.koin.androidx.compose.getViewModel

@Composable
fun NoteDetailsScreen(
    navController: NavHostController,
    viewModel: NoteDetailsViewModel = getViewModel(),
    noteUid: Int
) {
    val uiState = viewModel.stateFlow.collectAsState().value
    LaunchedEffect(noteUid) {
        viewModel.getNoteByUid(noteUid)
    }
    AppScaffold(
        topBar = {
            AppBackToolbar(stringResource(R.string.note_details)) {
                navController.popBackStack()
            }
        },
        content = {
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
        Spacer(Modifier.height(25.dp))
        Text(text = note.title, style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(20.dp))
        Text(text = note.description, style = MaterialTheme.typography.body1)
        Spacer(Modifier.height(25.dp))
        note.pictureName?.let {
            val url = "http://" + BuildConfig.HOST + ":" + BuildConfig.PORT + "/" + it
            CoilImage(
                data = url,
                contentDescription = "",
                fadeIn = true,
                contentScale = ContentScale.Crop,
                loading = { ProgressBar() },
                modifier = Modifier.size(300.dp)
            )
        }
    }
}
