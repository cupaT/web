package com.example.springfirst.repository

import com.example.springfirst.dto.UserData
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class InMemoryUserRepository : UserRepository {

    private val users: MutableMap<UUID, UserData> = mutableMapOf()

    override fun save(userData: UserData): UUID {
        val id = UUID.randomUUID()
        users[id] = userData
        return id
    }

    override fun findById(id: UUID): UserData? {
        return users[id]
    }
}
