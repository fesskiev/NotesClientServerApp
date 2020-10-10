package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

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
        when (e) {
            is SocketTimeoutException -> return "Timeout error"
            is IOException -> return "Network error"
//            is HttpException -> {
//                e.response()?.let { response ->
//                    if (response.code() == 401) {
//                        return "Authorized error"
//                    }
//                }
//            }
            else -> return "Unknown error"
        }
        return "Unknown error"
    }
}