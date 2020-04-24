package com.readify.userprofile.application.common

import com.readify.userprofile.domain.user.Email
import com.readify.userprofile.domain.user.FullName
import com.readify.userprofile.domain.user.Image
import com.readify.userprofile.domain.user.User
import com.readify.userprofile.domain.user.UserId
import com.readify.userprofile.domain.user.Username
import com.readify.userprofile.domain.user.Website

class UserMother {
    fun validUser(id: String) = User(UserId(id), Username(username), Email(email), FullName(fullName), Image(image),
        Website(website))

    companion object {
        private val username = "username"
        private val email = "email@emailprovider.com"
        private val fullName = "User Name"
        private val image = "http://username.jpg"
        private val website = "https://www.username.com"
    }
}