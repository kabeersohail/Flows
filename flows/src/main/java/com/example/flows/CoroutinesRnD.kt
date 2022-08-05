package com.example.flows

import kotlinx.coroutines.*

class CoroutinesRnD {

    suspend fun runInSequence() {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            firstMethod()
            secondMethod()
            thirdMethod()
            fourthMethod()
        }
    }

    suspend fun firstMethod() {

        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            delay(5000L)
            println("is executed before first")
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000L)
            println("firstMethod 2")
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000L)
            println("firstMethod 3")
        }

        delay(1000L)
        println("first")
    }

    suspend fun secondMethod() {
        delay(5000L)
        println("second")
    }

    suspend fun thirdMethod() {
        delay(2000L)
        println("third")
    }

    suspend fun fourthMethod() {
        println("fourth")
    }

}