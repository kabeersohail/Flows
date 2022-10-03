package com.example.flows.commandscheduler.models

enum class IncomingCommand {
    KIOSK,
    UNKIOSK,
    UNINSTALL
}

data class Command(val incomingCommand: IncomingCommand, val commandID: Long, val channel: Channel)