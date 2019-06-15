package com.github.novotnyr.idea.rabbitmq;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.execution.Location;
import com.intellij.execution.PsiLocation;
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

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import static com.github.novotnyr.idea.rabbitmq.PsiUtils.NO_INDEX;

public class YamlLineProvider implements LineMarkerProvider {
    private final Logger LOG = Logger.getInstance("#com.github.novotnyr.idea.rabbitmq.YamlLineProvider");

    private static final LineMarkerInfo NO_LINE_MARKER = null;

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (!PsiUtils.isRabbitMqScript(psiElement)) {
            return NO_LINE_MARKER;
        }
        if ("---".equals(psiElement.getText())) {
            int scriptIndex = PsiUtils.getScriptIndex(psiElement);
            if (scriptIndex == NO_INDEX) {
                return NO_LINE_MARKER;
            }
            GutterIconNavigationHandler navigationHandler = new GutterIconNavigationHandler() {
                @Override
                public void navigate(MouseEvent mouseEvent, PsiElement psiElement) {
                    PsiFile psiFile = psiElement.getContainingFile();
                    executeRabbitMqScript(psiFile, psiElement, scriptIndex);
                }
            };

            LineMarkerInfo<PsiElement> lineMarkerInfo = new LineMarkerInfo<PsiElement>(
                    psiElement, psiElement.getTextRange(), AllIcons.Toolwindows.ToolWindowRun, Pass.EXTERNAL_TOOLS, null, navigationHandler, GutterIconRenderer.Alignment.CENTER);
            return lineMarkerInfo;
        }
        return NO_LINE_MARKER;
    }

    private void executeRabbitMqScript(PsiFile rabbitMqScriptPsiFile, PsiElement psiElement, int scriptIndex) {
        RunRabbitMqScriptAction runRabbitMqScriptAction = (RunRabbitMqScriptAction) ActionManager.getInstance()
                .getAction(RunRabbitMqScriptAction.ID);
        PsiFileDataContext dataContext = new PsiFileDataContext(rabbitMqScriptPsiFile) {
            @Nullable
            @Override
            public Object getData(String dataId) {
                if (Location.DATA_KEY.is(dataId)) {
                    return new PsiLocation<>(psiElement.getProject(), psiElement);
                }
                return super.getData(dataId);
            }
        };
        AnActionEvent event = AnActionEvent.createFromDataContext(ActionPlaces.UNKNOWN, null, dataContext);
        runRabbitMqScriptAction.actionPerformed(event);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }
}
