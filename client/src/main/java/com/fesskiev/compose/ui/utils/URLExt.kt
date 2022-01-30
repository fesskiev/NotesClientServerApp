package com.fesskiev.compose.ui.utils

import com.fesskiev.compose.BuildConfig

fun String.toPictureUrl() = "http://" + BuildConfig.HOST + ":" + BuildConfig.PORT + "/" + this
