package com.fesskiev.compose.domain

import com.fesskiev.ServerErrorCodes.DISPLAY_NAME_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_INVALID
import com.fesskiev.ServerErrorCodes.PASSWORD_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_INVALID
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.domain.exceptions.UserInputException
import com.fesskiev.compose.ui.utils.FieldValidator
import com.fesskiev.model.JWTAuth

class RegistrationUseCase(
    private val repository: Repository,
    private val validator: FieldValidator
) {

    suspend operator fun invoke(
        email: String,
        displayName: String,
        password: String
    ): Result<JWTAuth> =
        try {
            if (validator.emptyDisplayName(displayName)) {
                throw UserInputException(DISPLAY_NAME_EMPTY)
            }
            if (validator.emptyEmail(email)) {
                throw UserInputException(EMAIL_EMPTY)
            }
            if (validator.emptyPassword(password)) {
                throw UserInputException(PASSWORD_EMPTY)
            }
            if (!validator.validateEmail(email)) {
                throw UserInputException(EMAIL_INVALID)
            }
            if (!validator.validatePassword(password)) {
                throw UserInputException(PASSWORD_INVALID)
            }
            val result = repository.registration(email, displayName, password)
            Result.Success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(e)
        }
}