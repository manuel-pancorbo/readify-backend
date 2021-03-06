package com.readify.api.userprofile.controller.common

import com.readify.userprofile.application.common.UserInformationResponse


class UserInformationResponseMother {
    fun existentUser(id: String) = UserInformationResponse(id,
        username,
        email,
        fullName,
        image,
        website
    )

    companion object {
        private val username = "username"
        private val email = "email@emailprovider.com"
        private val fullName = "User Name"
        private val image = "http://username.jpg"
        private val website = "https://www.username.com"
    }
}