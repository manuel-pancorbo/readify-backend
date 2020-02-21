package com.readify.userprofile.domain.user

import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.user.PlainUserCredentialsCreated
import com.readify.shared.domain.event.user.UserCreated
import com.readify.userprofile.domain.usercredentials.PlainPassword
import java.util.UUID

class User(val id: UserId, val username: Username, val email: Email, val password: PlainPassword? = null) :
    RootAggregate() {
    companion object {
        fun create(id: UserId, username: Username, email: Email, password: PlainPassword) =
            User(id, username, email, password)
                .also { it.record(UserCreated(id.value, username.value, email.value)) }
                .also { it.record(PlainUserCredentialsCreated(id.value, username.value, email.value, password.value)) }
    }
}

data class Username(val value: String)
data class Email(val value: String)
data class UserId(val value: String) {
    init {
        UUID.fromString(value)
    }
}
