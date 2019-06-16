package com.github.novotnyr.idea.rabbitmq;

import com.github.novotnyr.scotch.RabbitConfiguration;
import com.github.novotnyr.scotch.command.script.RabbitConfigurationParser;
import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PsiFileRabbitConfigurationProvider {
    private final YAMLFile yamlFile;

    private RabbitConfigurationParser rabbitConfigurationParser = new RabbitConfigurationParser();

    public PsiFileRabbitConfigurationProvider(YAMLFile yamlFile) {
        this.yamlFile = yamlFile;
    }

    public Optional<RabbitConfiguration> getRabbitConfiguration() {
        List<YAMLDocument> documents = yamlFile.getDocuments();
        if (documents.isEmpty()) {
            return Optional.empty();
        }
        YAMLDocument firstDocument = documents.get(0);
        List<YAMLPsiElement> yamlElements = firstDocument.getYAMLElements();
        if (yamlElements.isEmpty()) {
            return Optional.empty();
        }
        PsiElement yamlMappingPsiElement = yamlElements.get(0);
        if (!(yamlMappingPsiElement instanceof YAMLMapping)) {
            return Optional.empty();
        }
        YAMLMapping yamlMapping = (YAMLMapping) yamlMappingPsiElement;
        Map<String, Object> configurationMap = new LinkedHashMap<>();
        for (YAMLKeyValue keyValue : yamlMapping.getKeyValues()) {
            configurationMap.put(keyValue.getKeyText(), keyValue.getValueText());
        }
        RabbitConfiguration rabbitConfiguration = this.rabbitConfigurationParser.parseConfiguration(configurationMap);
        return Optional.of(rabbitConfiguration);
    }

    public void setRabbitConfigurationParser(RabbitConfigurationParser rabbitConfigurationParser) {
        this.rabbitConfigurationParser = rabbitConfigurationParser;
    }
}
