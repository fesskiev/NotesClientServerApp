package com.fesskiev.compose.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun AppBackToolbar(title: String, backOnClick: () -> Unit) {
    TopAppBar(title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = { backOnClick() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            })
}

@Composable
fun AppHamburgerToolbar(title: String, hamburgerOnClick: () -> Unit) {
    TopAppBar(title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = { hamburgerOnClick() }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
                }
            })
}