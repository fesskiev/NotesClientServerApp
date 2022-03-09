package com.fesskiev.compose

import android.app.Application
import com.fesskiev.compose.di.appModules
import com.fesskiev.compose.state.UiStateSaver
import com.fesskiev.compose.ui.utils.ThemeManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private val uiStateSaver: UiStateSaver by inject()
    private val themeManager by inject<ThemeManager>()

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        MainScope().launch {
            themeManager.setThemeMode()
            uiStateSaver.clearUiState()
        }
    }

    private fun setupKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModules)
        }
    }
}