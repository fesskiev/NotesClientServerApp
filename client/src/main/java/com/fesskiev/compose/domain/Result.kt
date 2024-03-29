package com.fesskiev.compose.domain

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val e: Exception) : Result<T>()
}