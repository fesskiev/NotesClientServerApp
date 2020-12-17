package com.fesskiev.compose.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.fesskiev.compose.presentation.AddNoteViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun EditNoteScreen(navController: NavHostController, viewModel: AddNoteViewModel = getViewModel(), noteUid: Int) {

}