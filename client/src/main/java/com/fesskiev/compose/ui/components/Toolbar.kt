package com.fesskiev.compose.ui.components

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.IconButton
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
                    Icon(Icons.Filled.ArrowBack)
                }
            })
}

@Composable
fun AppHamburgerToolbar(title: String, hamburgerOnClick: () -> Unit) {
    TopAppBar(title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = { hamburgerOnClick() }) {
                    Icon(Icons.Filled.Menu)
                }
            })
}