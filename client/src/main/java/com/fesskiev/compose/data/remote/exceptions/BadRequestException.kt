package com.fesskiev.compose.data.remote.exceptions

import androidx.annotation.StringRes
import java.io.IOException

class BadRequestException(@StringRes val resourceId: Int) : IOException()