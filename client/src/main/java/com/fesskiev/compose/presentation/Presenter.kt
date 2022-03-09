package com.fesskiev.compose.presentation

import androidx.compose.runtime.RememberObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class Presenter : RememberObserver {

    protected val coroutineScope: CoroutineScope = MainScope()

    open fun onCreate() {}

    open fun onDestroy() {}

    override fun onRemembered() {
        onCreate()
    }

    override fun onAbandoned() {
        coroutineScope.cancel()
        onDestroy()
    }

    override fun onForgotten() {
        coroutineScope.cancel()
        onDestroy()
    }
}