package com.fesskiev.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R

@Composable
fun EmptyView(message: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Image(
            modifier = Modifier.size(72.dp, 72.dp).align(Alignment.CenterHorizontally),
            painter = painterResource(R.drawable.ic_list),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = message,
            style = MaterialTheme.typography.body1
        )
    }
}