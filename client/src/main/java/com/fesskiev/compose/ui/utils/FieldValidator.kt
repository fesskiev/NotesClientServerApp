package com.fesskiev.compose.ui.utils

import java.util.regex.Pattern

class FieldValidator {

    companion object {
        private const val EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
    }

    private val pattern: Pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE)

    fun emptyEmail(email: String): Boolean = email.isEmpty()

    fun emptyPassword(password: String): Boolean = password.isEmpty()

    fun emptyDisplayName(name: String): Boolean = name.isEmpty()

    fun validateEmail(email: String): Boolean {
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun validatePassword(password: String): Boolean = password.length >=5
}