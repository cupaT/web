package com.example.springfirst.repository

import com.example.springfirst.dto.UserData
import java.util.UUID

interface UserRepository {
    fun save(userData: UserData): UUID
    fun findById(id: UUID): UserData?
}
