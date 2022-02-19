package com.fesskiev.compose.data

enum class PagingSource {
    LOCAL, REMOTE
}

data class PagingResult<T>(val list: List<T>, val pagingSource: PagingSource)