package com.github.novotnyr.idea.rabbitmq.console

import com.github.novotnyr.scotch.command.GetMessage
import com.github.novotnyr.scotch.command.script.ScriptOutputSerializer
import com.github.novotnyr.scotch.domain.RetrievedMessage

class GetMessageOutputSerializer(val stdOut: StdOut) : ScriptOutputSerializer<GetMessage, List<RetrievedMessage>> {
    override fun serialize(command: GetMessage, output: List<RetrievedMessage>) {
        for (message in output) {
            val sb = StringBuilder()
            sb.append("Messages left in Queue: ${message.messagesLeftInQueue}\n")
            sb.append("Payload Size: ${message.payloadSize}\n")
            sb.append("Redelivered: ").append(if (message.isRedelivered) "yes" else "no").append("\n")
            sb.append("Exchange: ${message.exchange}\n")
            val headers = message.properties.headers
            if (headers.isNotEmpty()) {
                sb.append("Headers:\n")
                for ((name, value) in headers) {
                    sb.append("  ${name}:${value}\n")
                }
            }
            sb.append("Payload: ${message.payload}")

            stdOut.println(sb.toString())
        }
    }
}
