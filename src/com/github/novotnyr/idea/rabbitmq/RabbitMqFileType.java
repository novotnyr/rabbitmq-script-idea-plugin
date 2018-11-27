package com.github.novotnyr.idea.rabbitmq;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLLanguage;

import javax.swing.Icon;

public class RabbitMqFileType extends LanguageFileType {
    public static final RabbitMqFileType RABBITMQ = new RabbitMqFileType();

    protected RabbitMqFileType() {
        super(YAMLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "RabbitMQ";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "RabbitMQ Script";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "rabbitmq";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.Nodes.DataTables;
    }
}
