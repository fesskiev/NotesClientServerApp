package com.fesskiev.compose.data.remote.exceptions

import java.io.IOException

class BadRequestException(val serverErrorCode: Int) : IOException()