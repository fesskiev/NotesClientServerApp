package com.fesskiev.compose.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.fesskiev.compose.R
import com.fesskiev.compose.mvi.NotesUiState
import com.fesskiev.compose.paging.getHttpErrorResourceOrNull
import com.fesskiev.compose.paging.isLoading
import com.fesskiev.compose.presentation.NotesViewModel
import com.fesskiev.compose.ui.components.AppDrawer
import com.fesskiev.compose.ui.components.AppScaffold
import com.fesskiev.compose.ui.components.AppToolbar
import com.fesskiev.compose.ui.navigation.AuthGraph
import com.fesskiev.compose.ui.navigation.MainGraph
import com.fesskiev.compose.ui.navigation.currentRoute
import com.fesskiev.compose.ui.navigation.currentScreenByRoute
import com.fesskiev.compose.ui.utils.getImageFileFromIntent
import com.fesskiev.compose.ui.utils.pickImageChooserIntent
import com.fesskiev.model.Note
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(viewModel: NotesViewModel = getViewModel()) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { data ->
                context.getImageFileFromIntent(data)?.let {
                    viewModel.attachAddNoteImage(it)
                }
            }
        }
    val noteItems: LazyPagingItems<Note> = viewModel.notesPagingStateFlow.collectAsLazyPagingItems()
    val uiState by viewModel.uiStateFlow.collectAsState()

    MainScaffold(
        uiState = uiState,
        noteItems = noteItems,
        onNoteClick = {
            viewModel.openNoteDetails(it)
        },
        onNoteEditClick = {
            viewModel.openEditNoteScreen(it)
        },
        onEditNoteChangedTitle = {
            viewModel.changeEditNoteTitle(it)
        },
        onEditNoteChangedDescription = {
            viewModel.changeEditNoteDescription(it)
        },
        onNoteEditedClick = {
            viewModel.editNote()
        },
        onAddNoteClick = {
            viewModel.addNote()
        },
        onAddNoteChangedTitle = {
            viewModel.changeAddNoteTitle(it)
        },
        onAddNoteChangedDescription = {
            viewModel.changeAddNoteDescription(it)
        },
        onNoteDelete = {
            viewModel.deleteNote(it)
        },
        onDeleteImageClick = {
            viewModel.deleteAddNoteImage()
        },
        onFabClick = {
            viewModel.openAddNoteScreen()
        },
        onPickImageClick = {
            launcher.launch(context.pickImageChooserIntent(title = "Pick Image"))
        }
    )
}

@Composable
fun MainScaffold(
    uiState: NotesUiState,
    noteItems: LazyPagingItems<Note>,
    onNoteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    onEditNoteChangedTitle: (String) -> Unit,
    onEditNoteChangedDescription: (String) -> Unit,
    onNoteEditedClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onAddNoteChangedTitle: (String) -> Unit,
    onAddNoteChangedDescription: (String) -> Unit,
    onNoteDelete: (Note) -> Unit,
    onDeleteImageClick: () -> Unit,
    onFabClick: () -> Unit,
    onPickImageClick: () -> Unit
) {

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val currentScreen = navController.currentRoute()?.currentScreenByRoute()
    AppScaffold(
        scope = scope,
        topBar = {
            if (currentScreen != null) {
                AppToolbar(
                    currentScreen = currentScreen,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onHamburgerClick = {
                        scope.launch {
                            it.drawerState.open()
                        }
                    },
                    onAddNoteClick = onAddNoteClick,
                    onPickImageClick = onPickImageClick
                )
            }
        },
        drawerContent = {
            AppDrawer(
                onSettingsClick = {
                    scope.launch {
                        it.drawerState.close()
                        navController.navigate(MainGraph.SettingsScreen.route)
                    }
                }
            )
        },
        drawerGesturesEnabled = currentScreen is MainGraph.NotesListScreen,
        floatingActionButton = {
            if (currentScreen is MainGraph.NotesListScreen) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(MainGraph.AddNoteScreen.route)
                        onFabClick()
                    }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_plus_one),
                        contentDescription = ""
                    )
                }
            }
        },
        errorResourceId = uiState.errorResourceId ?: noteItems.getHttpErrorResourceOrNull()
    ) { innerPadding ->
        if (uiState.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            NavHost(navController = navController, startDestination = MainGraph.NotesListScreen.route) {
                composable(MainGraph.NotesListScreen.route) {
                    NotesListScreen(
                        noteItems,
                        noteItems.isLoading(),
                        onNoteClick = {
                            onNoteClick(it)
                            navController.navigate(MainGraph.NoteDetailsScreen.route)
                        },
                        onNoteDeleteClick = onNoteDelete,
                        onNoteEditClick = {
                            onNoteEditClick(it)
                            navController.navigate(MainGraph.EditNoteScreen.route)
                        }
                    )
                }
                composable(MainGraph.EditNoteScreen.route) {
                    EditNoteScreen(
                        uiState = uiState,
                        onEditedNoteClick = onNoteEditedClick,
                        onEditNoteChangedTitle = onEditNoteChangedTitle,
                        onEditNoteChangedDescription = onEditNoteChangedDescription,
                        onScreenClose = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(MainGraph.AddNoteScreen.route) {
                    AddNoteScreen(
                        uiState = uiState,
                        onAddNoteChangedTitle = onAddNoteChangedTitle,
                        onAddNoteChangedDescription = onAddNoteChangedDescription,
                        onDeleteImageClick = onDeleteImageClick,
                        onScreenClose = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(MainGraph.NoteDetailsScreen.route) {
                    NoteDetailsScreen(uiState = uiState)
                }
                composable(MainGraph.SettingsScreen.route) {
                    SettingsScreen(
                        onShowThemeDialogClick = { navController.navigate(MainGraph.SettingsThemeDialog.route) },
                        onLogoutClick = { navController.navigate(AuthGraph.route) }
                    )
                }
                dialog(MainGraph.SettingsThemeDialog.route) {
                    ThemeDialog(
                        onDismissClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

