package com.github.novotnyr.idea.rabbitmq;

import com.github.novotnyr.idea.rabbitmq.editor.FileProfileService;
import com.github.novotnyr.idea.rabbitmq.settings.PluginSettings;
import com.github.novotnyr.idea.rabbitmq.settings.RabbitProfile;
import com.github.novotnyr.scotch.RabbitConfiguration;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.Optional;

public class RabbitMqScriptRunConfiguration extends LocatableConfigurationBase {
    private PsiFile rabbitMqScriptPsiFile;
    private int scriptIndex;

    protected RabbitMqScriptRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SettingsEditor() {
            @Override
            protected void resetEditorFrom(@NotNull Object o) {

            }

            @Override
            protected void applyEditorTo(@NotNull Object o) throws ConfigurationException {

            }

            @NotNull
            @Override
            protected JComponent createEditor() {
                return new JLabel("NO SETTINGS");
            }
        };
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        RabbitConfiguration rabbitConfiguration = getRabbitConfiguration();
        return new RabbitMqRunProfileState(this.rabbitMqScriptPsiFile, this.scriptIndex, rabbitConfiguration);
    }

    private RabbitConfiguration getRabbitConfiguration() {
        PluginSettings pluginSettings = ServiceManager.getService(getProject(), PluginSettings.class);
        FileProfileService fileProfileService = ServiceManager.getService(getProject(), FileProfileService.class);
        Optional<String> profile = fileProfileService.getProfile(this.rabbitMqScriptPsiFile.getVirtualFile().getPath());
        if (profile.isPresent()) {
            Optional<RabbitProfile> maybeRabbitProfile = pluginSettings.findProfileByName(profile.get());
            if (maybeRabbitProfile.isPresent()) {
                return maybeRabbitProfile.get().getRabbitConfiguration();
            } else {
                return null;
            }
        }
        return null;
    }

    public void setRabbitMqScriptPsiFile(PsiFile rabbitMqScriptPsiFile) {
        this.rabbitMqScriptPsiFile = rabbitMqScriptPsiFile;
    }

    public void setScriptIndex(int scriptIndex) {
        this.scriptIndex = scriptIndex;
    }

    public int getScriptIndex() {
        return scriptIndex;
    }
}
