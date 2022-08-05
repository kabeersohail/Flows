package com.example.flows.commandscheduler

import com.example.flows.commandscheduler.models.Channel
import com.example.flows.commandscheduler.models.Command
import com.example.flows.commandscheduler.models.IncomingCommand.KIOSK
import com.example.flows.commandscheduler.models.IncomingCommand.UNKIOSK
import com.example.flows.commandscheduler.models.Reason
import com.example.flows.commandscheduler.states.KioskLockState
import io.mockk.coVerify
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IncomingCommandSchedulerTest {

    private lateinit var commandScheduler: CommandScheduler

    @Before
    fun setup() {
        commandScheduler = spyk(CommandScheduler())
    }

    // Tailored scenario
    @Test
    fun `when command history is empty and same command is received from two different channels with same id, first received command must be executed and the second received command must not be executed`() = runTest {

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 1234,Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 1234,Channel.RTDB))

        verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, 1234,Channel.FCM), Reason.COMMAND_HISTORY_WAS_EMPTY) }
        verify(exactly = 1) { commandScheduler.commandNotExecuted(Command(KIOSK, 1234,Channel.RTDB), Reason.COMMAND_WITH_SAME_ID_EXISTS_IN_COMMAND_HISTORY) }
    }

    // Tailored scenario
    @Test
    fun `when command history is not empty and same command is received from two different channels with same id, first received command must be executed and the second received command must not be executed`() = runTest {

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.commandHistory[1L] = KIOSK

        commandScheduler.schedule(Command(KIOSK, 1234,Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 1234,Channel.RTDB))

        verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, 1234,Channel.FCM), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY) }
        verify(exactly = 1) { commandScheduler.commandNotExecuted(Command(KIOSK, 1234,Channel.RTDB), Reason.COMMAND_WITH_SAME_ID_EXISTS_IN_COMMAND_HISTORY) }
    }

    // Tailored scenario
    @Test
    fun `when same command is received multiple times from same channel with different ID's, then commands must be scheduled all the time `() = runTest {

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.commandHistory[100L] = KIOSK

        commandScheduler.schedule(Command(KIOSK, 1, Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 2, Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 3, Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 4, Channel.FCM))

        for(index in 1L..4L){
            verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, index, Channel.FCM), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY) }
        }
    }

    // Tailored scenario
    @Test
    fun `when same command is received multiple times from different channels with different ID's, then commands must be scheduled all the time `() = runTest {

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.commandHistory[100L] = KIOSK

        commandScheduler.schedule(Command(KIOSK, 1, Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 2, Channel.RTDB))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 3, Channel.FCM))

        // Setting the scenario
        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 4, Channel.RTDB))

        verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, 1, Channel.FCM), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY) }
        verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, 2, Channel.RTDB), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY) }
        verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, 3, Channel.FCM), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY) }
        verify(exactly = 1) { commandScheduler.commandExecuted(Command(KIOSK, 4, Channel.RTDB), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY) }

    }

    @Test
    fun `when a command is received with same ID which is present in command history but the actual commands are different, then command must be executed `() = runTest {

        commandScheduler.deviceState.kioskLockState = KioskLockState.Locked

        commandScheduler.commandHistory[100L] = KIOSK

        commandScheduler.schedule(Command(UNKIOSK, 100L, Channel.FCM))

        verify(exactly = 1) { commandScheduler.commandExecuted(Command(UNKIOSK, 100, Channel.FCM), Reason.A_COMMAND_WITH_SAME_ID_EXISTS_BUT_DIFFERENT_FROM_CURRENT_INCOMING_COMMAND) }

    }

    @Test
    fun `when a command is received from manual channel, then it must be executed in any scenario`() = runTest {

        commandScheduler.deviceState.kioskLockState = KioskLockState.Locked

        commandScheduler.commandHistory[100L] = KIOSK

        commandScheduler.schedule(Command(UNKIOSK, 0,Channel.MANUAL))

        verify(exactly = 1) { commandScheduler.commandExecuted(Command(UNKIOSK, 0,Channel.MANUAL), Reason.MANUAL_CHANNEL) }

    }

    @Test
    fun `when a command with same id is received from different channels, then first command must be executed and subsequent command must not be executed`() = runTest {

        // initial state

        commandScheduler.commandHistory[100L] = KIOSK

        commandScheduler.deviceState.kioskLockState = KioskLockState.Unlocked

        commandScheduler.schedule(Command(KIOSK, 1L, Channel.RTDB))

        commandScheduler.schedule(Command(KIOSK, 1L, Channel.FCM))

        coVerify(exactly = 1) {
            commandScheduler.commandExecuted(Command(KIOSK, 1L, Channel.RTDB), Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY)
            commandScheduler.commandNotExecuted(Command(KIOSK, 1L, Channel.FCM), Reason.DEVICE_IS_ALREADY_IN_COMMANDED_STATE)
        }

    }

}