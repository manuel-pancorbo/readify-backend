package com.readify.userprofile.domain.user

data class UsernameAlreadyRegisteredException(val username: String) :
    Throwable("Username $username is already registered")
