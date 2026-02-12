package com.example.springfirst.dto

import java.util.UUID

data class GreetingUser(
    val text: String,
    val id: UUID
)
