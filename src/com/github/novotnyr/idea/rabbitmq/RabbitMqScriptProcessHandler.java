package com.github.novotnyr.idea.rabbitmq;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.github.novotnyr.rabbitmqadmin.command.ExecuteScript;
import com.github.novotnyr.rabbitmqadmin.command.GetMessage;
import com.github.novotnyr.rabbitmqadmin.command.script.GetMessageStdErrOutputSerializer;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.psi.PsiFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

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
        String scriptFile = scriptPsiFile.getVirtualFile().getPath();
        ExecuteScript executeScript = new ExecuteScript(this.rabbitConfiguration);
        executeScript.setScriptFile(scriptFile);
        executeScript.setIncludedCommandIndices(Arrays.asList(this.scriptIndex));
        StdErr stdErr = new StdErr() {
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
        configureOutputSerializers(executeScript, stdErr);
        executeScript.run();
        notifyTextAvailable("RabbitMQ script completed", ProcessOutputTypes.SYSTEM);
        notifyProcessTerminated(0);
        return NOTHING;
    }

    private void configureOutputSerializers(ExecuteScript executeScript, StdErr stdErr) {
        executeScript.setOutputSerializer(GetMessage.class, new GetMessageStdErrOutputSerializer(stdErr));
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
