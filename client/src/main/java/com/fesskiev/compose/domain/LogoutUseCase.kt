package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository

class LogoutUseCase(private val repository: Repository) {

    suspend operator fun invoke(): Result<Unit> =
        try {
            repository.logout()
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(e)
        }
}