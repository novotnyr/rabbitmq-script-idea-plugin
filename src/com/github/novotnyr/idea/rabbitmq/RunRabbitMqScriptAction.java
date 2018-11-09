package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class RunRabbitMqScriptAction extends AnAction {
    public static final String ID = "com.github.novotnyr.idea.rabbitmq.RunRabbitMqScriptAction";

    @Override
    public void actionPerformed(AnActionEvent event) {
        PsiFile file = CommonDataKeys.PSI_FILE.getData(event.getDataContext());
        if (file == null) {
            return;
        }
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        ConfigurationContext context = ConfigurationContext.getFromContext(event.getDataContext());
        ConfigurationFromContext fromContext = RunConfigurationProducer.getInstance(RabbitMqScriptRunConfigurationProducer.class)
                .createConfigurationFromContext(context);
        if (fromContext == null) {
            return;
        }
        RunnerAndConfigurationSettings settings = fromContext.getConfigurationSettings();
        RunManager runManager = RunManager.getInstance(project);
        runManager.setSelectedConfiguration(settings);
        ExecutionEnvironmentBuilder builder = ExecutionEnvironmentBuilder.createOrNull(DefaultRunExecutor.getRunExecutorInstance(), settings);
        if (builder != null) {
            ExecutionManager.getInstance(project).restartRunProfile(builder.build());
        }
    }
}
