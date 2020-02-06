package com.readify.userprofile.domain.user

data class EmailAlreadyRegisteredException(val email: String) :
    Throwable("Email $email is already registered")