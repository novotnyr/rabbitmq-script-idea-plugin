package com.github.novotnyr.idea.rabbitmq;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

public class YamlLineProvider implements LineMarkerProvider {
    private final Logger LOG = Logger.getInstance("#com.github.novotnyr.idea.rabbitmq.YamlLineProvider");

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (psiElement instanceof YAMLKeyValue) {
            YAMLKeyValue yamlKeyValuePsiElement = (YAMLKeyValue) psiElement;
            if ("host".equals(yamlKeyValuePsiElement.getKeyText())) {
                GutterIconNavigationHandler navigationHandler = new GutterIconNavigationHandler() {
                    @Override
                    public void navigate(MouseEvent mouseEvent, PsiElement psiElement) {
                        PsiFile psiFile = psiElement.getContainingFile();
                        executeRabbitMqScript(psiFile);
                    }
                };

                LineMarkerInfo<YAMLKeyValue> lineMarkerInfo = new LineMarkerInfo<YAMLKeyValue>(
                        yamlKeyValuePsiElement, yamlKeyValuePsiElement.getTextRange(), AllIcons.Toolwindows.ToolWindowRun, Pass.EXTERNAL_TOOLS, null, navigationHandler, GutterIconRenderer.Alignment.CENTER);
                return lineMarkerInfo;
            }
        }
        return null;
    }

    private void executeRabbitMqScript(PsiFile rabbitMqScriptPsiFile) {
        RunRabbitMqScriptAction runRabbitMqScriptAction = (RunRabbitMqScriptAction) ActionManager.getInstance()
                .getAction(RunRabbitMqScriptAction.ID);
        AnActionEvent event = AnActionEvent.createFromDataContext(ActionPlaces.UNKNOWN, null, new PsiFileDataContext(rabbitMqScriptPsiFile));
        runRabbitMqScriptAction.actionPerformed(event);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }
}
