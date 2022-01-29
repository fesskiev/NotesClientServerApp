package com.fesskiev.compose.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fesskiev.compose.domain.GetNotesUseCase
import com.fesskiev.model.Note

class NotesSource(private val getNotesUseCase: GetNotesUseCase) : PagingSource<Int, Note>() {

    override fun getRefreshKey(state: PagingState<Int, Note>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Note> =
        try {
            val pageNumber = params.key ?: 1
            val notes = getNotesUseCase(pageNumber)
            LoadResult.Page(
                data = notes,
                prevKey = null,
                nextKey = if (notes.isNotEmpty()) pageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}