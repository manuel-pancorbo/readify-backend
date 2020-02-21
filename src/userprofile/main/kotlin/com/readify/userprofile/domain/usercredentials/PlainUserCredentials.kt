package com.readify.userprofile.domain.usercredentials

import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.Username

data class PlainUserCredentials(val id: UserId, val email: Email, val username: Username, val password: PlainPassword)

data class PlainPassword(val value: String)