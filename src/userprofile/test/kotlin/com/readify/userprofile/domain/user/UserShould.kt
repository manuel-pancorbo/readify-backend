package com.readify.userprofile.domain.user

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.readify.shared.domain.event.user.PlainUserCredentialsCreated
import com.readify.shared.domain.event.user.UserCreated
import com.readify.userprofile.domain.usercredentials.PlainPassword
import org.junit.jupiter.api.Test
import java.util.UUID

class UserShould {
    @Test
    fun `user should record a usercreated domain event`() {
        val anyId = UUID.randomUUID().toString()
        val user = User.create(UserId(anyId), Username("manu"), Email("manu@gmail.com"), FullName("Manuel Pancorbo"),
            Image("any image"), Website("https://manuel.pancorbo"), PlainPassword("123456"))

        assertThat(user.pullDomainEvents()[0]).isInstanceOf(UserCreated::class.java)
        assertThat(user.pullDomainEvents()[0].occurredOn).isNotNull()
        assertThat((user.pullDomainEvents()[0] as UserCreated).userId).isEqualTo(anyId)
        assertThat((user.pullDomainEvents()[0] as UserCreated).username).isEqualTo("manu")
        assertThat((user.pullDomainEvents()[0] as UserCreated).email).isEqualTo("manu@gmail.com")
    }

    @Test
    fun `user should record a plain user credentials domain event`() {
        val anyId = UUID.randomUUID().toString()
        val user = User.create(UserId(anyId), Username("manu"), Email("manu@gmail.com"), FullName("Manuel Pancorbo"),
            Image("any image"), Website("https://manuel.pancorbo"), PlainPassword("123456"))

        assertThat(user.pullDomainEvents()[1]).isInstanceOf(PlainUserCredentialsCreated::class.java)
        assertThat(user.pullDomainEvents()[1].occurredOn).isNotNull()
        assertThat((user.pullDomainEvents()[1] as PlainUserCredentialsCreated).userId).isEqualTo(anyId)
        assertThat((user.pullDomainEvents()[1] as PlainUserCredentialsCreated).username).isEqualTo("manu")
        assertThat((user.pullDomainEvents()[1] as PlainUserCredentialsCreated).email).isEqualTo("manu@gmail.com")
        assertThat((user.pullDomainEvents()[1] as PlainUserCredentialsCreated).plainPassword).isEqualTo("123456")
    }

    @Test
    internal fun `default constructor does not record any domain event`() {
        val anyId = UUID.randomUUID().toString()
        val user = User(UserId(anyId), Username("manu"), Email("manu@gmail.com"), FullName("Manuel Pancorbo"),
            Image("any image"), Website("https://manuel.pancorbo"), PlainPassword("123456"))

        assertThat(user.pullDomainEvents()).isEmpty()
    }
}