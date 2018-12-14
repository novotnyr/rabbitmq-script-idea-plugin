package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class RabbitMqScriptConfigurationType extends ConfigurationTypeBase {

    private final ConfigurationFactory factory;

    protected RabbitMqScriptConfigurationType() {
        super("com.github.novotnyr.rabbitmq.RabbitMqScriptConfigurationType", "RabbitMQ", "Run RabbitMQ", (Icon) null);
        this.factory = new ConfigurationFactory(this) {
            @NotNull
            @Override
            public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                RabbitMqScriptRunConfiguration runConfiguration = new RabbitMqScriptRunConfiguration(project, this, "");
                return runConfiguration;
            }
        };
        addFactory(this.factory);
    }

    public static RabbitMqScriptConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(RabbitMqScriptConfigurationType.class);
    }

    public ConfigurationFactory getFactory() {
        return this.factory;
    }
}
