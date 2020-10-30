package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.UnauthorizedException
import io.ktor.client.features.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    fun launchDataLoad(load: suspend () -> Unit, error: (String) -> Unit): Job = viewModelScope.launch {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            error(parseError(e))
        }
    }

    private fun parseError(e: Exception) : String  {
        return when (e) {
            is HttpRequestTimeoutException -> "Timeout error"
            is UnauthorizedException -> e.message
            else -> "Unknown error"
        }
    }
}