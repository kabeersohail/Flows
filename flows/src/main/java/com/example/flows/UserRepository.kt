package com.example.flows

import kotlinx.coroutines.delay

class UserRepository {
    private var users = mutableListOf<String>()

    suspend fun register(name: String) {
        delay(100L)
        users += name
        println("Registered $name")
    }

    suspend fun getAllUsers(): List<String> {
        delay(100L)
        return users
    }
}