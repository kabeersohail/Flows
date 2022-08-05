package com.example.flows

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class ChannelFlowRnD {

    lateinit var link: Link
    var state: Int = 0

    private val produce: Flow<Int> = channelFlow {
        link = object : Link {
            override suspend fun emitValue(value: Int) {
                send(value)
            }
        }
    }

    suspend fun collect(){
        produce.collect {
            println(it)
            state = it
        }
    }

}

interface Link {
    suspend fun emitValue(value: Int)
}