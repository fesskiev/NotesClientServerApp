package com.fesskiev.compose.domain

import com.fesskiev.compose.data.remote.RemoteService

class LogoutUseCase(private val remoteService: RemoteService) {

    suspend operator fun invoke(): Result<Unit> =
        try {
            remoteService.logout()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
}