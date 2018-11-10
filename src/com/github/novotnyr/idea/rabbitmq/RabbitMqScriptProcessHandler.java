package com.github.novotnyr.idea.rabbitmq;

import com.github.novotnyr.rabbitmqadmin.command.ExecuteScript;
import com.github.novotnyr.rabbitmqadmin.log.StdErr;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.psi.PsiFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class RabbitMqScriptProcessHandler extends CallableProcessHandler {
    private final PsiFile scriptPsiFile;
    private final int scriptIndex;

    public RabbitMqScriptProcessHandler(PsiFile scriptPsiFile, int scriptIndex) {
        this.scriptPsiFile = scriptPsiFile;
        this.scriptIndex = scriptIndex;
    }

    @Override
    protected Void doCall() {
        String scriptFile = scriptPsiFile.getVirtualFile().getPath();
        ExecuteScript executeScript = new ExecuteScript(null);
        executeScript.setScriptFile(scriptFile);
        executeScript.setIncludedCommandIndices(Arrays.asList(this.scriptIndex));
        executeScript.setStdErr(new StdErr() {
            @Override
            public void println(String message) {
                StringBuilder text = new StringBuilder(message);
                if (!message.endsWith(System.lineSeparator())) {
                    text.append(System.lineSeparator());
                }
                notifyTextAvailable(text.toString(), ProcessOutputTypes.STDOUT);
            }
        });
        executeScript.run();
        notifyTextAvailable("RabbitMQ script completed", ProcessOutputTypes.STDOUT);
        notifyProcessTerminated(0);
        return NOTHING;
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
