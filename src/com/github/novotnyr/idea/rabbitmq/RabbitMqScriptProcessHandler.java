package com.github.novotnyr.idea.rabbitmq;

import com.github.novotnyr.idea.rabbitmq.console.PublishToExchangeOutputSerializer;
import com.github.novotnyr.idea.rabbitmq.console.StdOut;
import com.github.novotnyr.scotch.RabbitConfiguration;
import com.github.novotnyr.scotch.RabbitMqAccessDeniedException;
import com.github.novotnyr.scotch.RabbitMqConnectionException;
import com.github.novotnyr.scotch.command.GetMessage;
import com.github.novotnyr.scotch.command.PublishToExchange;
import com.github.novotnyr.scotch.command.script.ExecuteScript;
import com.github.novotnyr.scotch.command.script.StdErr;
import com.github.novotnyr.scotch.command.script.ser.GetMessageStdErrOutputSerializer;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.psi.PsiFile;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

public class RabbitMqScriptProcessHandler extends CallableProcessHandler {
    private final PsiFile scriptPsiFile;
    private final int scriptIndex;
    private final RabbitConfiguration rabbitConfiguration;

    public RabbitMqScriptProcessHandler(PsiFile scriptPsiFile, int scriptIndex, RabbitConfiguration rabbitConfiguration) {
        this.scriptPsiFile = scriptPsiFile;
        this.scriptIndex = scriptIndex;
        this.rabbitConfiguration = rabbitConfiguration;
    }

    @Override
    protected Void doCall() {
        if (!isValidConfiguration()) {
            return NOTHING;
        }
        String scriptFile = this.scriptPsiFile.getVirtualFile().getPath();
        ExecuteScript executeScript = new ExecuteScript(this.rabbitConfiguration);
        executeScript.setScriptFile(scriptFile);
        executeScript.setIncludedCommandIndices(Collections.singletonList(this.scriptIndex));
        StdErr stdErr = new StdErr() {
            @Override
            public void println(String message) {
                StringBuilder text = new StringBuilder(message);
                if (!message.endsWith(System.lineSeparator())) {
                    text.append(System.lineSeparator());
                }
                notifyTextAvailable(text.toString(), ProcessOutputTypes.STDERR);
            }
        };
        StdOut stdOut = new StdOut() {
            @Override
            public void println(String message) {
                StringBuilder text = new StringBuilder(message);
                if (!message.endsWith(System.lineSeparator())) {
                    text.append(System.lineSeparator());
                }
                notifyTextAvailable(text.toString(), ProcessOutputTypes.STDOUT);
            }
        };
        executeScript.setStdErr(stdErr);
        configureOutputSerializers(executeScript, stdOut, stdErr);
        run(executeScript);
        return NOTHING;
    }

    private void run(ExecuteScript executeScript) {
        try {
            executeScript.run(new Continuation<Unit>() {
                @NotNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NotNull Object o) {
                    notifyTextAvailable("RabbitMQ script completed", ProcessOutputTypes.SYSTEM);
                    notifyProcessTerminated(0);
                }
            });
       } catch (RabbitMqConnectionException e) {
            notifyTextAvailable(e.getMessage(), ProcessOutputTypes.STDERR);
        } catch (RabbitMqAccessDeniedException e) {
            notifyTextAvailable("Access denied to RabbitMQ broker. Please verify the credentials", ProcessOutputTypes.STDERR);
            notifyProcessTerminated(3);
        }
    }

    private boolean isValidConfiguration() {
        if (this.rabbitConfiguration == null) {
            notifyTextAvailable("No RabbitMQ configuration is provided. Please configure it in a script file or create a profile", ProcessOutputTypes.STDERR);
            notifyProcessTerminated(1);
            return false;
        }
        return true;
    }

    private void configureOutputSerializers(ExecuteScript executeScript, StdOut stdOut, StdErr stdErr) {
        executeScript.setOutputSerializer(GetMessage.class, new GetMessageStdErrOutputSerializer(stdErr));
        executeScript.setOutputSerializer(PublishToExchange.class, new PublishToExchangeOutputSerializer(stdOut, stdErr));
    }

    @Override
    protected Void handleException(Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        notifyTextAvailable(out.toString(), ProcessOutputTypes.STDERR);
        notifyProcessTerminated(1);
        return NOTHING;
    }


}
