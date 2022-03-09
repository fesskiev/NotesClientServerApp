package com.fesskiev.compose.presentation

import androidx.compose.runtime.mutableStateOf
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LogoutUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.domain.ThemeModeUseCase
import com.fesskiev.compose.state.ErrorState
import com.fesskiev.compose.state.SettingsUiState
import com.fesskiev.compose.ui.utils.update
import kotlinx.coroutines.launch

class SettingsPresenter(
    private val logoutUseCase: LogoutUseCase,
    private val themeModeUseCase: ThemeModeUseCase
) : Presenter() {

    val settingsUiState = mutableStateOf(SettingsUiState())

    override fun onCreate() {
        coroutineScope.launch {
            val themeMode = themeModeUseCase.getThemeMode()
            settingsUiState.update {
                it.copy(
                    loading = false,
                    isLogout = false,
                    themeMode = themeMode,
                    error = null
                )
            }
        }
    }

    fun setThemeMode(themeMode: String) {
        coroutineScope.launch {
            themeModeUseCase.setThemeMode(themeMode)
            settingsUiState.update {
                it.copy(
                    loading = false,
                    isLogout = false,
                    themeMode = themeMode,
                    error = null
                )
            }
        }
    }

    fun logout() {
        coroutineScope.launch {
            settingsUiState.apply {
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