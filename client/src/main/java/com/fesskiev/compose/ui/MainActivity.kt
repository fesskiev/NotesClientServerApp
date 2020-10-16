package com.fesskiev.compose.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.NotesViewModel
import com.fesskiev.compose.ui.screens.registration.RegistrationScreen
import com.fesskiev.compose.ui.utils.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val repository by inject<Repository>()
    private val navigationViewModel by viewModel<NotesViewModel>(state = { Bundle() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                RegistrationScreen()
            }
        }
//        navigationViewModel.notesLiveData.observe(this, { notes ->
//            Log.wtf("test", "notes: $notes")
//        })
//        navigationViewModel.getNotes()
//        GlobalScope.launch {
//            try {
//                repository.registration("fesskiev@gmail.com", "Max", "123456")
//                repeat(10) {
//                    repository.addNote("text test")
//                }
//                val notes = repository.getNotes()
//                repository.deleteNote(notes.first())
//
//                repository.getNotes()
//                repository.updateNote(notes.first().copy(text = "copy text"))
//
//                repository.logout()
//                repository.getNotes()
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
    }
}