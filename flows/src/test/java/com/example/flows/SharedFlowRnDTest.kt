package com.example.flows

import com.example.flows.commandscheduler.models.Channel
import com.example.flows.commandscheduler.models.Command
import com.example.flows.commandscheduler.models.IncomingCommand
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharedFlowRnDTest {
    private lateinit var sharedFlowRnD: SharedFlowRnD

    @Before
    fun setup() {
        sharedFlowRnD = spyk(SharedFlowRnD())
    }


    @Test
    fun `first test`() = runTest {

        val job = this.launch{ sharedFlowRnD.collectCommands() }
        advanceUntilIdle()

        sharedFlowRnD.singleListener.onDataChange(
            Command(IncomingCommand.KIOSK, 1L, Channel.RTDB)
        )

        sharedFlowRnD.singleListener.onMessageReceived(
            Command(IncomingCommand.KIOSK, 1L, Channel.FCM)
        )

        sharedFlowRnD.singleListener.manualChannel(
            Command(IncomingCommand.KIOSK, 1L, Channel.FCM)
        )

        job.cancel()

        verify(exactly = 3) { sharedFlowRnD.commandExecuted() }
    }

}