package com.example.springfirst.controller

import com.example.springfirst.dto.GreetingMain
import com.example.springfirst.dto.GreetingUser
import com.example.springfirst.dto.UserData
import com.example.springfirst.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/greeting")
class HelloController(
    private val userRepository: UserRepository
) {

    @GetMapping
    fun hello(@RequestParam(required = false) id: UUID?): ResponseEntity<Any> {
        if (id == null) {
            return ResponseEntity.ok(GreetingMain())
        }

        val user = userRepository.findById(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(user)
    }

    @PostMapping
    fun createUser(@RequestBody userData: UserData): ResponseEntity<GreetingUser> {
        val id = userRepository.save(userData)
        val text = "Hello, ${userData.surname} ${userData.name}"
        val response = GreetingUser(text = text, id = id)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<UserData> {
        val user = userRepository.findById(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(user)
    }
}
