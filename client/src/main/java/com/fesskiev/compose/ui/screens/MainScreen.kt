package com.fesskiev.compose.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(viewModel: NotesViewModel = getViewModel(), onCloseAppClick: () -> Unit,) {
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
        onCloseAppClick = onCloseAppClick,
        onRefresh = { viewModel.refresh() },
        onNoteClick = { viewModel.openNoteDetails(it) },
        onNoteEditClick = { viewModel.openEditNoteScreen(it) },
        onEditNoteChangedTitle = { viewModel.changeEditNoteTitle(it) },
        onEditNoteChangedDescription = { viewModel.changeEditNoteDescription(it) },
        onNoteEditedClick = { viewModel.editNote() },
        onAddNoteClick = { viewModel.addNote() },
        onAddNoteChangedTitle = { viewModel.changeAddNoteTitle(it) },
        onAddNoteChangedDescription = { viewModel.changeAddNoteDescription(it) },
        onNoteDelete = { viewModel.deleteNote(it) },
        onDeleteImageClick = { viewModel.deleteAddNoteImage() },
        onFabClick = { viewModel.openAddNoteScreen() },
        onPickImageClick = { launcher.launch(context.pickImageChooserIntent(title = "Pick Image")) }
    )
}

@Composable
fun MainScaffold(
    uiState: NotesUiState,
    noteItems: LazyPagingItems<Note>,
    onCloseAppClick: () -> Unit,
    onRefresh: () -> Unit,
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
        onBackPressed = {
            navController.navigate(MainGraph.ExitDialog.route)
        },
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
        bottomBar = {
            if (currentScreen != null && (currentScreen is MainGraph.NotesListScreen || currentScreen is MainGraph.NotesSearchScreen)
            ) {
                BottomBar(navController)
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
            NavHost(
                navController = navController,
                startDestination = MainGraph.NotesListScreen.route
            ) {
                composable(MainGraph.NotesListScreen.route) {
                    SwipeRefreshNotesList(
                        uiState,
                        noteItems,
                        onRefresh = onRefresh,
                        onNoteClick = {
                            onNoteClick(it)
                            navController.navigate(MainGraph.NoteDetailsScreen.route)
                        },
                        onNoteEditClick = {
                            onNoteEditClick(it)
                            navController.navigate(MainGraph.EditNoteScreen.route)
                        },
                        onNoteDelete
                    )
                }
                composable(MainGraph.NotesSearchScreen.route) {
                    NotesSearchScreen()
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
                    ThemeDialog()
                }
                dialog(MainGraph.ExitDialog.route) {
                    ExitDialog(onCloseAppClick = onCloseAppClick)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val items = listOf(
        MainGraph.NotesListScreen,
        MainGraph.NotesSearchScreen,
    )
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.iconId),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(screen.label)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun SwipeRefreshNotesList(
    uiState: NotesUiState,
    noteItems: LazyPagingItems<Note>,
    onRefresh: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = uiState.refresh),
        onRefresh = onRefresh,
    ) {
        NotesListScreen(
            noteItems,
            noteItems.isLoading(),
            onNoteClick = onNoteClick,
            onNoteDeleteClick = onNoteDelete,
            onNoteEditClick = onNoteEditClick
        )
    }
}

