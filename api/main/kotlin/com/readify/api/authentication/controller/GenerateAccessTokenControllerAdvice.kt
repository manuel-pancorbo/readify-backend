package com.readify.api.authentication.controller

import com.readify.authentication.domain.usercredentials.InvalidUserCredentialsException
import com.readify.shared.infrastructure.controller.error.HttpErrorResponse
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

const val INVALID_CREDENTIALS_ERROR_CODE = "auth.invalid-credentials"

@ControllerAdvice
class GenerateAccessTokenControllerAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(InvalidUserCredentialsException::class)
    fun handleConflictWithEmail(exception: InvalidUserCredentialsException): ResponseEntity<HttpErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                HttpErrorResponse(
                    INVALID_CREDENTIALS_ERROR_CODE,
                    "Invalid credentials provided"
                )
            )
}
