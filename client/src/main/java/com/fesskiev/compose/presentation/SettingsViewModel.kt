package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LogoutUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.domain.ThemeModeUseCase
import com.fesskiev.compose.mvi.SettingsUiState
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
                            errorResourceId = null
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
                            errorResourceId = null
                        )
                    }
                }.catch { e ->
                    uiStateFlow.update {
                        it.copy(
                            loading = false,
                            isLogout = false,
                            errorResourceId = parseHttpError(e)
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
                        errorResourceId = null
                    )
                }
                val result = logoutUseCase()
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                isLogout = true,
                                errorResourceId = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                isLogout = false,
                                errorResourceId = parseHttpError(result.e)
                            )
                        }
                    }
                }
            }
        }
    }
}