package com.github.novotnyr.idea.rabbitmq.console;


import com.github.novotnyr.scotch.command.PublishToExchange;
import com.github.novotnyr.scotch.command.api.PublishToExchangeResponse;
import com.github.novotnyr.scotch.command.script.ScriptOutputSerializer;
import com.github.novotnyr.scotch.command.script.StdErr;

public class PublishToExchangeOutputSerializer implements ScriptOutputSerializer<PublishToExchange, PublishToExchangeResponse> {
    private final StdOut stdOut;
    private final StdErr stdErr;

    public PublishToExchangeOutputSerializer(StdOut stdOut, StdErr stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public void serialize(PublishToExchange command, PublishToExchangeResponse response) {
        String messagePrefix = "Message " + formatDescription(command)
                + "was sent to '" + command.getExchange() + "'" + " exchange "
                + "(routing key: '" + command.getRoutingKey() + "')";
        if (response.isRouted()) {
            this.stdOut.println(messagePrefix + " and successfully routed");
        } else {
            this.stdErr.println(messagePrefix + ", but not routed. Is there at least 1 binding to this exchange?");
        }
    }

    private String formatDescription(PublishToExchange command) {
        String desc = command.getDescription();
        if (desc != null) {
            return "'" + desc + "' ";
        }
        return "";
    }
}
