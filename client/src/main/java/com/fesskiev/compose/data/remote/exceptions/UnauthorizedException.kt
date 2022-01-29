package com.fesskiev.compose.data.remote.exceptions

import java.io.IOException

class UnauthorizedException(val serverErrorCode: Int) : IOException()