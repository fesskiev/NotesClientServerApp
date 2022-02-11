package com.fesskiev.compose.ui.utils

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> =
    map {
        if (block(it)) newValue else it
    }


fun <T> List<T>.plusTop(newValue: T): List<T> =
    toMutableList()
        .apply {
            add(0, newValue)
        }

