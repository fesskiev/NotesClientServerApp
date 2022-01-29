package com.fesskiev.compose.ui.utils

import com.fesskiev.compose.BuildConfig

fun String.toImageUrl() = "http://" + BuildConfig.HOST + ":" + BuildConfig.PORT + "/" + this
