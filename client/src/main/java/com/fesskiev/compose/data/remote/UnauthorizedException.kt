package com.fesskiev.compose.data.remote

import androidx.annotation.StringRes
import java.io.IOException

class UnauthorizedException(@StringRes val resourceId: Int) : IOException()