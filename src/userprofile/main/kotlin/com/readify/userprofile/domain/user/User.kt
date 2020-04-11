package com.readify.userprofile.domain.user

import com.readify.shared.domain.event.RootAggregate
import com.readify.shared.domain.event.user.PlainUserCredentialsCreated
import com.readify.shared.domain.event.user.UserCreated
import com.readify.userprofile.domain.usercredentials.PlainPassword
import java.util.UUID

class User(val id: UserId, val username: Username, val email: Email, val fullName: FullName, val image: Image?,
           val website: Website?, val password: PlainPassword? = null) :
    RootAggregate() {
    companion object {
        fun create(id: UserId, username: Username, email: Email, fullName: FullName, image: Image?, website: Website?,
                   password: PlainPassword) =
            User(id, username, email, fullName, image, website, password)
                .also { it.record(UserCreated(id.value, username.value, email.value, fullName.value)) }
                .also { it.record(PlainUserCredentialsCreated(id.value, username.value, email.value, password.value)) }
    }
}

data class Username(val value: String)
data class Email(val value: String)
data class FullName(val value: String)
data class Image(val value: String)
data class Website(val value: String)
data class UserId(val value: String) {
    init {
        UUID.fromString(value)
    }
}
