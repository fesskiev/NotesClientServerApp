package com.fesskiev.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R

@Composable
fun EmptyView(message: String) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Image(
            modifier = Modifier.size(72.dp, 72.dp).align(Alignment.CenterHorizontally),
            imageVector = vectorResource(R.drawable.ic_list)
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = message,
            style = MaterialTheme.typography.body1
        )
    }
}