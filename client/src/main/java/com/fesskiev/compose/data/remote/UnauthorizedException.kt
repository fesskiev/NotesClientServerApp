package com.fesskiev.compose.data.remote

import java.io.IOException

class UnauthorizedException(override val message: String) : IOException(message)