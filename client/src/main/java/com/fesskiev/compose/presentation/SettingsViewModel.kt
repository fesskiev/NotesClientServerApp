package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LogoutUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.domain.ThemeModeUseCase
import com.fesskiev.compose.state.ErrorState
import com.fesskiev.compose.state.SettingsUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val themeModeUseCase: ThemeModeUseCase
) : ViewModel() {

    val uiStateFlow = MutableStateFlow(SettingsUiState())

    init {
        viewModelScope.launch {
            themeModeUseCase.getThemeMode()
                .collect { themeMode ->
                    uiStateFlow.update {
                        it.copy(
                            loading = false,
                            isLogout = false,
                            themeMode = themeMode,
                            error = null
                        )
                    }
                }
        }
    }

    fun setThemeMode(themeMode: String) {
        viewModelScope.launch {
            themeModeUseCase.setThemeMode(themeMode)
                .onStart {
                    uiStateFlow.update {
                        it.copy(
                            loading = false,
                            isLogout = false,
                            themeMode = themeMode,
                            error = null
                        )
                    }
                }.catch { e ->
                    uiStateFlow.update {
                        it.copy(
                            loading = false,
                            isLogout = false,
                            error = ErrorState(errorResourceId = parseHttpError(e))
                        )
                    }

                }.collect()
        }
    }

    fun logout() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update {
                    it.copy(
                        loading = true,
                        isLogout = false,
                        error = null
                    )
                }
                val result = logoutUseCase()
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                isLogout = true,
                                error = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                isLogout = false,
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }
}