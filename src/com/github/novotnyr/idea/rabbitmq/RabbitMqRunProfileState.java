package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RabbitMqRunProfileState implements RunProfileState {
    private final PsiFile scriptPsiFile;
    private final int scriptIndex;

    public RabbitMqRunProfileState(PsiFile scriptPsiFile, int scriptIndex) {
        this.scriptPsiFile = scriptPsiFile;
        this.scriptIndex = scriptIndex;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
        ProcessHandler processHandler = new RabbitMqScriptProcessHandler(this.scriptPsiFile, this.scriptIndex);
        ConsoleView console = createExecutionConsole();
        console.attachToProcess(processHandler);
        return new DefaultExecutionResult(console, processHandler);
    }

    private ConsoleView createExecutionConsole() {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(getProject());
        return consoleBuilder.getConsole();
    }

    protected Project getProject() {
        return this.scriptPsiFile.getProject();
    }
}
