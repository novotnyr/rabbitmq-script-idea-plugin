package com.github.novotnyr.idea.rabbitmq.console;

import com.github.novotnyr.rabbitmqadmin.command.PublishToExchangeResponse;
import com.github.novotnyr.rabbitmqadmin.command.script.ScriptOutputSerializer;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;

public class PublishToExchangeOutputSerializer implements ScriptOutputSerializer<PublishToExchangeResponse> {
    private final StdOut stdOut;
    private final StdErr stdErr;

    public PublishToExchangeOutputSerializer(StdOut stdOut, StdErr stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    @Override
    public void serialize(Class<?> commandClass, PublishToExchangeResponse response) {
        if (response.isRouted()) {
            this.stdOut.println("Message was successfully routed.");
        } else {
            this.stdErr.println("Message has not been routed. Is there at least 1 binding to this exchange?");
        }
    }
}
