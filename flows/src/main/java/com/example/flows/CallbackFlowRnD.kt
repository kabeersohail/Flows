package com.example.flows

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class CallbackFlowRnD {

    @VisibleForTesting
    internal lateinit var event: Event

    internal var state1: Int = 0
    internal var state2: Int = 0

    private val produce = callbackFlow {

        event = object : Event {
            override suspend fun produce(int: Int) {
                send(int)
            }
        }
        awaitClose()
    }

    suspend fun firstCollect() = produce.collect {
        println("first collect $it")
        state1 += it
    }

    suspend fun secondCollect() = produce.collect {
        println("second collect $it")
        state2 += it
    }

}

interface Event {
    suspend fun produce(int: Int)
}