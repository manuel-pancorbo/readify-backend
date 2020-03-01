package com.readify.authentication.domain

sealed class Requester

object AnonymousUser : Requester()
data class LoggedUser(val id: String, val username: String, val email: String) : Requester()