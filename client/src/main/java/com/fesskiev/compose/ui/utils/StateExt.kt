package com.fesskiev.compose.ui.utils

import androidx.compose.runtime.MutableState

inline fun <T> MutableState<T>.update(function: (T) -> T) {
    val prevValue = value
    val nextValue = function(prevValue)
    value = nextValue
}