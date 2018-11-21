package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.yaml.YAMLFileType;

public class RabbitMqScriptRunConfigurationProducer extends com.intellij.execution.actions.RunConfigurationProducer<RabbitMqScriptRunConfiguration> {
    public RabbitMqScriptRunConfigurationProducer() {
        super(RabbitMqScriptConfigurationType.getInstance().getFactory());
    }

    @Override
    protected boolean setupConfigurationFromContext(RabbitMqScriptRunConfiguration runConfiguration, ConfigurationContext configurationContext, Ref<PsiElement> ref) {
        if (ref == null) {
            return false;
        }
        PsiElement psiElement = ref.get();
        if (psiElement == null) {
            return false;
        }

        PsiFile scriptFile = psiElement.getContainingFile();
        if (!(scriptFile instanceof YAMLFileType)) {
            return false;
        }
        runConfiguration.setName(scriptFile.getName());
        runConfiguration.setRabbitMqScriptPsiFile(scriptFile);
        Location location = configurationContext.getLocation();
        if (location != null) {
            PsiElement element = location.getPsiElement();
            runConfiguration.setScriptIndex(PsiUtils.getScriptIndex(element));
        }
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(RabbitMqScriptRunConfiguration runConfiguration, ConfigurationContext configurationContext) {
        return true;
    }
}
