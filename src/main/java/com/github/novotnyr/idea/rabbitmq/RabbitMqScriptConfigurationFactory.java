package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class RabbitMqScriptConfigurationFactory extends ConfigurationFactory {
    protected RabbitMqScriptConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new RabbitMqScriptRunConfiguration(project, this, "RabbitMQ Script");
    }
}
