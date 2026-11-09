package com.example.app.repository

import com.example.app.model.User
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class UserRepository {
    suspend fun fetchUser(): User {
        delay(duration = 1000.milliseconds)
        return  User(name ="Manantena")
    }
}