package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class RabbitMqScriptConfigurationType implements ConfigurationType {

    public static RabbitMqScriptConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(RabbitMqScriptConfigurationType.class);
    }

    @Override
    public String getDisplayName() {
        return "RabbitMQ Script";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Run RabbitMQ Script";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.RunConfigurations.Unknown;
    }

    @NotNull
    @Override
    public String getId() {
        return "com.github.novotnyr.rabbitmq.RabbitMqScriptConfigurationType";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{
                new RabbitMqScriptConfigurationFactory(this)
        };
    }

    public RabbitMqScriptConfigurationFactory getFactory() {
        return (RabbitMqScriptConfigurationFactory) getConfigurationFactories()[0];
    }
}
