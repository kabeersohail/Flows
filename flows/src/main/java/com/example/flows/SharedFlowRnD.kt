package com.example.flows

import androidx.annotation.VisibleForTesting
import com.example.flows.commandscheduler.models.Command
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SharedFlowRnD {

    private val _incomingCommand = MutableSharedFlow<Command>()
    private val incomingCommand: SharedFlow<Command> = _incomingCommand

    val singleListener: SingleListener = object : SingleListener {

        override suspend fun onMessageReceived(command: Command) {
            _incomingCommand.emit(command)
        }

        override suspend fun onDataChange(command: Command) {
            _incomingCommand.emit(command)
        }

        override suspend fun manualChannel(command: Command) {
            _incomingCommand.emit(command)
        }
    }

    suspend fun collectCommands() {
        incomingCommand.collect { command ->
            println("received ${command.incomingCommand.name} from ${command.channel.name}")
            commandExecuted()
        }
    }

    @VisibleForTesting
    internal fun commandExecuted() {}

}

interface SingleListener {
    suspend fun onMessageReceived(command: Command)
    suspend fun onDataChange(command: Command)
    suspend fun manualChannel(command: Command)
}