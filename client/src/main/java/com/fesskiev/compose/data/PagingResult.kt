package com.fesskiev.compose.data

import kotlinx.serialization.Serializable

@Serializable
enum class PagingSource  {
    LOCAL, REMOTE
}

data class PagingResult<T>(val list: List<T>, val pagingSource: PagingSource)