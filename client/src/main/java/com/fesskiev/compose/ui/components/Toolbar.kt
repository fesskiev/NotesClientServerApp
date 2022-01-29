package com.fesskiev.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    TopAppBar(
        title = { Text(stringResource(currentScreen.resourceId)) },
        navigationIcon = {
            when (currentScreen) {
                is MainGraph.NotesListScreen -> {
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
            if (currentScreen is MainGraph.AddNoteScreen) {
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
        }
    )
}