package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LogoutUseCase
import com.fesskiev.compose.domain.ThemeModeUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val themeModeUseCase: ThemeModeUseCase
) : ViewModel() {

    val stateFlow = MutableStateFlow(SettingsUiState())

    init {
        viewModelScope.launch {
            themeModeUseCase.getThemeMode()
                .collect {
                    stateFlow.value = stateFlow.value.copy(
                        loading = false,
                        isLogout = false,
                        themeMode = it,
                        isThemeModePopupShow = false,
                        errorResourceId = null
                    )
                }
        }
    }

    fun setThemeMode(themeMode: String) {
        viewModelScope.launch {
            themeModeUseCase.setThemeMode(themeMode)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(
                        loading = false,
                        isLogout = false,
                        themeMode = themeMode,
                        isThemeModePopupShow = false,
                        errorResourceId = null
                    )
                }.catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            isLogout = false,
                            isThemeModePopupShow = false,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect()
        }
    }

    fun hideThemeModePopup() {
        stateFlow.value =
            stateFlow.value.copy(loading = false, isLogout = false, isThemeModePopupShow = false)
    }

    fun showThemeModePopup() {
        stateFlow.value =
            stateFlow.value.copy(loading = false, isLogout = false, isThemeModePopupShow = true)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.logout()
                .onStart {
                    stateFlow.value = stateFlow.value.copy(
                        loading = true,
                        isLogout = false,
                        isThemeModePopupShow = false,
                        errorResourceId = null
                    )
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            isLogout = false,
                            isThemeModePopupShow = false,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value = stateFlow.value.copy(loading = false, isLogout = true)
                }
        }
    }
}