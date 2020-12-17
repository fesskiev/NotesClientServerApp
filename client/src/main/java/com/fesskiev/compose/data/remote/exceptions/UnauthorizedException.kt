package com.fesskiev.compose.data.remote.exceptions

import androidx.annotation.StringRes
import java.io.IOException

class UnauthorizedException(@StringRes val resourceId: Int) : IOException()