package com.fesskiev.compose.paging

import androidx.annotation.StringRes
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.fesskiev.compose.data.remote.parseHttpError

fun <T : Any> LazyPagingItems<T>.isEmpty(): Boolean = itemCount == 0

fun <T : Any> LazyPagingItems<T>.isLoading(): Boolean =
    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading

fun <T : Any> LazyPagingItems<T>.getError(): LoadState.Error? = when {
    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
    else -> null
}

@StringRes
fun <T : Any> LazyPagingItems<T>.getHttpErrorResourceOrNull(): Int? {
    val errorState = getError()
    if (errorState?.error != null) {
        return parseHttpError(errorState.error)
    }
    return null
}