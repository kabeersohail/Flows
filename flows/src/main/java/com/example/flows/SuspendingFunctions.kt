package com.example.flows

import kotlinx.coroutines.delay

class SuspendingFunctions {
    suspend fun fetchData(): String {
        delay(1000L)
        return "Hello World"
    }
}