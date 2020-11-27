package com.fesskiev.compose.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.R
import com.fesskiev.compose.data.remote.BadRequestException
import com.fesskiev.compose.data.remote.UnauthorizedException
import io.ktor.client.features.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

open class BaseViewModel : ViewModel() {

    fun launchDataLoad(load: suspend () -> Unit, error: (Int) -> Unit): Job = viewModelScope.launch {
        try {
            load()
        } catch (e: Exception) {
            e.printStackTrace()
            error(parseError(e))
        }
    }

    @StringRes
    private fun parseError(e: Exception) : Int  {
        return when (e) {
            is HttpRequestTimeoutException -> R.string.error_timeout
            is ConnectException -> R.string.error_connect_server
            is UnauthorizedException -> e.resourceId
            is BadRequestException -> e.resourceId
            else -> R.string.error_unknown
        }
    }
}