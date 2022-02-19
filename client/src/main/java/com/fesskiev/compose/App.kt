package com.fesskiev.compose

import android.app.Application
import com.fesskiev.compose.di.appModules
import com.fesskiev.compose.ui.utils.DataStoreManager
import com.fesskiev.compose.ui.utils.ThemeManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    private val themeManager by inject<ThemeManager>()
    private val dataStoreManager by inject<DataStoreManager>()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModules)
        }

        initAsync()
    }

    private fun initAsync() {
        MainScope().launch {
            applyTheme()
        }
    }

    private suspend fun applyTheme() {
        dataStoreManager.getThemeMode()
            .collect {
                themeManager.applyNewTheme(it)
            }
    }
}