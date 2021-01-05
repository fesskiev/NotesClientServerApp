package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogoutUseCase(private val repository: Repository) {

    fun logout(): Flow<Boolean> = flow {
        repository.logout()
        emit(true)
    }
}