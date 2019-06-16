package com.github.novotnyr.idea.rabbitmq;

import com.github.novotnyr.scotch.RabbitConfiguration;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.psi.YAMLFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

public class PsiUtilsTest extends LightPlatformCodeInsightFixtureTestCase {
    @Test
    public void testDetectRabbitConfiguration() throws IOException {
        byte[] bytes = Files.readAllBytes(new File("src/test/resources/example.get.rabbitmq").toPath());
        String ymlString = new String(bytes, StandardCharsets.UTF_8);

        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(getProject());
        PsiFile yaml = psiFileFactory.createFileFromText("example.get.rabbitmq", YAMLFileType.YML, ymlString);
        if (yaml instanceof YAMLFile) {
            YAMLFile yamlFile = (YAMLFile) yaml;

            PsiFileRabbitConfigurationProvider rabbitConfigurationProvider = new PsiFileRabbitConfigurationProvider(yamlFile);
            Optional<RabbitConfiguration> maybeRabbitConfiguration = rabbitConfigurationProvider.getRabbitConfiguration();
            if (maybeRabbitConfiguration.isPresent()) {
                Assert.assertNotNull(maybeRabbitConfiguration.get());
            } else {
                Assert.fail("No Rabbit Configuration was found");
            }
        }
    }
}