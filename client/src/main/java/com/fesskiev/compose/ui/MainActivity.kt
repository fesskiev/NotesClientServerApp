package com.fesskiev.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fesskiev.compose.data.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val repository by inject<Repository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GlobalScope.launch {
            try {
                repository.registration("fesskiev@gmail.com", "Max", "123456")
                repeat(10) {
                    repository.addNote("text test")
                }
                val notes = repository.getNotes()
                repository.deleteNote(notes.first())

                repository.getNotes()
                repository.updateNote(notes.first().copy(text = "copy text"))

                repository.logout()
                repository.getNotes()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}