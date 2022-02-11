package com.fesskiev.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.ui.navigation.MainGraph
import com.fesskiev.compose.ui.navigation.Screen

@Composable
fun AppToolbar(
    currentScreen: Screen,
    onBackClick: () -> Unit,
    onHamburgerClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onPickImageClick: () -> Unit
) {
    val background =
        when {
            isSystemInDarkTheme() -> Color(0xFF2A2A2A)
            currentScreen is MainGraph.NotesSearchScreen -> Color.White.copy(alpha = 0.8f)
            else -> MaterialTheme.colors.primarySurface
        }
    TopAppBar(
        backgroundColor = background,
        title = { Text(stringResource(currentScreen.resourceId)) },
        navigationIcon = {
            when (currentScreen) {
                is MainGraph.NotesListScreen, MainGraph.NotesSearchScreen -> {
                    IconButton(onClick = { onHamburgerClick() }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
                    }
                }
                else -> {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                    }
                }
            }
        },
        actions = {
            when (currentScreen) {
                is MainGraph.AddNoteScreen -> {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable(onClick = onAddNoteClick)
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_image),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable(onClick = onPickImageClick)
                        )
                    }
                }
                is MainGraph.NotesSearchScreen -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(background)

                    ) {
                        TextField(
                            value = "",
                            placeholder = { Text("Search in notes") },
                            onValueChange = {},
                            modifier = Modifier.fillMaxSize(),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = background,
                                cursorColor = MaterialTheme.colors.onSurface,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    )
}