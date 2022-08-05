package com.example.flows.commandscheduler

import androidx.annotation.VisibleForTesting
import com.example.flows.commandscheduler.models.*
import com.example.flows.commandscheduler.states.DeviceState
import com.example.flows.commandscheduler.states.KioskLockState
import com.example.flows.commandscheduler.states.Status

class CommandScheduler {

    @VisibleForTesting
    internal val commandHistory: MutableMap<Long, IncomingCommand> = mutableMapOf()

    @VisibleForTesting
    internal val deviceState: DeviceState = DeviceState()

    @VisibleForTesting
    internal fun schedule(command: Command) {

        when(command.channel) {
            Channel.MANUAL -> execute(command, Reason.MANUAL_CHANNEL)
            else ->
            when(isDeviceAlreadyInCommandedState(command.incomingCommand)) {
                Status.YES -> commandNotExecuted(command, Reason.DEVICE_IS_ALREADY_IN_COMMANDED_STATE)
                Status.NO -> proceed(command)
                Status.UNKNOWN -> proceed(command)
            }
        }
    }

    private fun proceed(command: Command) {
        when (commandHistory.isEmpty()) {
            true -> execute(command, Reason.COMMAND_HISTORY_WAS_EMPTY)
            false -> {
                val existingCommandWithSameId: IncomingCommand =
                    checkIfAnyCommandExistForThisID(command.commandID) ?: run {
                        execute(command,
                            Reason.COMMAND_WITH_THIS_ID_IS_NOT_PRESENT_IN_COMMAND_HISTORY)
                        return
                    }

                if (existingCommandWithSameId == command.incomingCommand) {
                    commandNotExecuted(command,
                        Reason.COMMAND_WITH_SAME_ID_EXISTS_IN_COMMAND_HISTORY)
                } else {
                    execute(command,
                        Reason.A_COMMAND_WITH_SAME_ID_EXISTS_BUT_DIFFERENT_FROM_CURRENT_INCOMING_COMMAND)
                }
            }
        }
    }

    private fun isDeviceAlreadyInCommandedState(incomingCommand: IncomingCommand): Status = when(incomingCommand) {
        IncomingCommand.KIOSK -> when(deviceState.kioskLockState) {
            is KioskLockState.Locked -> Status.YES
            is KioskLockState.Unlocked -> Status.NO
            is KioskLockState.Unknown -> Status.UNKNOWN
        }
        IncomingCommand.UNKIOSK -> when(deviceState.kioskLockState) {
            is KioskLockState.Locked -> Status.NO
            is KioskLockState.Unlocked -> Status.YES
            is KioskLockState.Unknown -> Status.UNKNOWN
        }
    }

    private fun checkIfAnyCommandExistForThisID(commandID: Long): IncomingCommand? = when(commandHistory[commandID]) {
        IncomingCommand.KIOSK -> IncomingCommand.KIOSK
        IncomingCommand.UNKIOSK -> IncomingCommand.UNKIOSK
        null -> null
    }

    private fun execute(command: Command, reason: Reason) {
        when(command.incomingCommand) {
            IncomingCommand.KIOSK -> {
                deviceState.kioskLockState = KioskLockState.Locked
                commandExecuted(command, reason)
                commandHistory[command.commandID] = command.incomingCommand
            }
            IncomingCommand.UNKIOSK -> {
                deviceState.kioskLockState = KioskLockState.Unlocked
                commandExecuted(command, reason)
                commandHistory[command.commandID] = command.incomingCommand
            }
        }
    }

    internal fun commandNotExecuted(command: Command, reason: Reason) {
        println("${command.incomingCommand} was not executed because $reason")
    }

    internal fun commandExecuted(command: Command, reason: Reason) {
        println("${command.incomingCommand} was executed because $reason")
    }

}