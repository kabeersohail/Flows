package com.example.flows

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = UserRepository()
    }


    @Test
    fun `This test will fail`() = runTest {
        val userRepo = UserRepository()

        launch { userRepo.register("Alice") }
        launch { userRepo.register("Bob") }
        advanceUntilIdle()

        assertEquals(listOf("Alice", "Bob"), userRepo.getAllUsers())
    }

}