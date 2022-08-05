package com.example.flows

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelFlowRnDTest {

    lateinit var channelFlowRnD: ChannelFlowRnD

    @Before
    fun setup() {
        channelFlowRnD = ChannelFlowRnD()
    }

    @Test
    fun `guide to test channel flows`() = runTest {

        channelFlowRnD.collect()
        advanceUntilIdle()

        channelFlowRnD.link.emitValue(100)
        advanceUntilIdle()

        Assert.assertEquals(100, channelFlowRnD.state)

    }

}