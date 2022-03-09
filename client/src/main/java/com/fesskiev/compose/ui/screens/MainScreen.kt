package com.fesskiev.compose.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.fesskiev.compose.R
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.presentation.NotesPresenter
import com.fesskiev.compose.state.AddNoteUiState
import com.fesskiev.compose.state.EditNoteUiState
import com.fesskiev.compose.state.NotesListUiState
import com.fesskiev.compose.state.SearchNotesUiState
import com.fesskiev.compose.ui.components.AppDrawer
import com.fesskiev.compose.ui.components.AppScaffold
import com.fesskiev.compose.ui.components.AppToolbar
import com.fesskiev.compose.ui.navigation.MainGraph
import com.fesskiev.compose.ui.navigation.currentRoute
import com.fesskiev.compose.ui.navigation.currentScreenByRoute
import com.fesskiev.compose.ui.utils.getImageFileFromIntent
import com.fesskiev.compose.ui.utils.pickImageChooserIntent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun MainScreen(
    presenter: NotesPresenter = get(),
    onLogoutClick: () -> Unit,
    onCloseAppClick: () -> Unit
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { data ->
                context.getImageFileFromIntent(data)?.let {
                    presenter.attachAddNoteImage(it)
                }
            }
        }

    val addNoteUiState by presenter.addNoteUiState
    Log.wtf("state_add", addNoteUiState.toString())
    val notesListUiState by presenter.notesListUiState
    Log.wtf("state_list", notesListUiState.toString())
    val editNoteUiState by presenter.editNoteUiState
    Log.wtf("state_edit", editNoteUiState.toString())
    val searchNotesUiState by presenter.searchNotesUiState
    Log.wtf("state_search", searchNotesUiState.toString())

    MainScaffold(
        notesListUiState = notesListUiState,
        addNoteUiState = addNoteUiState,
        editNoteUiState = editNoteUiState,
        searchNotesUiState = searchNotesUiState,
        onCloseAppClick = onCloseAppClick,
        onLogoutClick = onLogoutClick,
        onRefresh = { presenter.refresh() },
        onLoadMore = { presenter.loadMore() },
        onRetryClick = { presenter.loadMore() },
        onNoteClick = { presenter.openNoteDetails(it) },
        onNoteEditClick = { presenter.openEditNoteScreen(it) },
        onEditNoteChangedTitle = { presenter.changeEditNoteTitle(it) },
        onEditNoteChangedDescription = { presenter.changeEditNoteDescription(it) },
        onNoteEditedClick = { presenter.editNote() },
        onAddNoteClick = { presenter.addNote() },
        onAddNoteChangedTitle = { presenter.changeAddNoteTitle(it) },
        onAddNoteChangedDescription = { presenter.changeAddNoteDescription(it) },
        onNoteDelete = { presenter.deleteNote(it) },
        onDeleteImageClick = { presenter.deleteAddNoteImage() },
        onFabClick = { presenter.openAddNoteScreen() },
        onPickImageClick = { launcher.launch(context.pickImageChooserIntent(title = "Pick Image")) },
        onSearchChanged = { presenter.searchNotes(it) },
    )
}

@Composable
fun MainScaffold(
    notesListUiState: NotesListUiState,
    addNoteUiState: AddNoteUiState,
    editNoteUiState: EditNoteUiState,
    searchNotesUiState: SearchNotesUiState,
    onCloseAppClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onRetryClick: () -> Unit,
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
    onPickImageClick: () -> Unit,
    onSearchChanged: (String) -> Unit
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
                    onPickImageClick = onPickImageClick,
                    onSearchChanged = onSearchChanged,
                    search = searchNotesUiState.query
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
        drawerGesturesEnabled = currentScreen is MainGraph.NotesListScreen || currentScreen is MainGraph.NotesSearchScreen,
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
        error = notesListUiState.error
    ) { innerPadding ->
        if (notesListUiState.refresh) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            NavHost(
                navController = navController,
                startDestination = MainGraph.NotesListScreen.route
            ) {
                composable(MainGraph.NotesListScreen.route) {
                    SwipeRefreshNotesList(
                        uiState = notesListUiState,
                        onRefresh = onRefresh,
                        onNoteClick = {
                            onNoteClick(it)
                            navController.navigate(MainGraph.NoteDetailsScreen.route)
                        },
                        onNoteEditClick = {
                            onNoteEditClick(it)
                            navController.navigate(MainGraph.EditNoteScreen.route)
                        },
                        onNoteDelete,
                        onLoadMore = onLoadMore,
                        onRetryClick = onRetryClick
                    )
                }
                composable(MainGraph.NotesSearchScreen.route) {
                    NotesSearchScreen(uiState = searchNotesUiState)
                }
                composable(MainGraph.EditNoteScreen.route) {
                    EditNoteScreen(
                        uiState = editNoteUiState,
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
                        uiState = addNoteUiState,
                        onAddNoteChangedTitle = onAddNoteChangedTitle,
                        onAddNoteChangedDescription = onAddNoteChangedDescription,
                        onDeleteImageClick = onDeleteImageClick,
                        onScreenClose = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(MainGraph.NoteDetailsScreen.route) {
                    NoteDetailsScreen(uiState = notesListUiState)
                }
                composable(MainGraph.SettingsScreen.route) {
                    SettingsScreen(
                        onShowThemeDialogClick = { navController.navigate(MainGraph.SettingsThemeDialog.route) },
                        onLogoutClick = onLogoutClick
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
    uiState: NotesListUiState,
    onRefresh: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit,
    onLoadMore: () -> Unit,
    onRetryClick: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = uiState.refresh),
        onRefresh = onRefresh,
    ) {
        NotesListScreen(
            uiState = uiState,
            onNoteClick = onNoteClick,
            onNoteDeleteClick = onNoteDelete,
            onNoteEditClick = onNoteEditClick,
            onLoadMore = onLoadMore,
            onRetryClick = onRetryClick
        )
    }
}

