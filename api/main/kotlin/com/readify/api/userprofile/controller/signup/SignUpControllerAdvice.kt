package com.readify.api.userprofile.controller.signup

import com.readify.shared.infrastructure.controller.error.HttpErrorResponse
import com.readify.userprofile.domain.user.EmailAlreadyRegisteredException
import com.readify.userprofile.domain.user.UsernameAlreadyRegisteredException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

const val DUPLICATED_EMAIL_ERROR_CODE = "signup.email"
const val DUPLICATED_USERNAME_ERROR_CODE = "signup.username"

@ControllerAdvice
class SignUpControllerAdvice : ResponseEntityExceptionHandler() {
    @ExceptionHandler(EmailAlreadyRegisteredException::class)
    fun handleConflictWithEmail(exception: EmailAlreadyRegisteredException): ResponseEntity<HttpErrorResponse> =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                HttpErrorResponse(
                    DUPLICATED_EMAIL_ERROR_CODE,
                    "An user with [${exception.email}] as email already exists",
                    "email"
                )
            )

    @ExceptionHandler(UsernameAlreadyRegisteredException::class)
    fun handleConflictWithEmail(exception: UsernameAlreadyRegisteredException): ResponseEntity<HttpErrorResponse> =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                HttpErrorResponse(
                    DUPLICATED_USERNAME_ERROR_CODE,
                    "An user with [${exception.username}] as username already exists",
                    "username"
                )
            )
}
