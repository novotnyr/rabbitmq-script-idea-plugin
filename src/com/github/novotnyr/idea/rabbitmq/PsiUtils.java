package com.github.novotnyr.idea.rabbitmq;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PsiUtils {
    public static final int NO_INDEX = -1;

    public static int getScriptIndex(@NotNull PsiElement psiElement) {
        if (!"---".equals(psiElement.getText())) {
            return NO_INDEX;
        }
        if (psiElement.getParent() == null) {
            return NO_INDEX;
        }
        PsiElement parent = psiElement.getParent();
        if (parent.getParent() == null) {
            return NO_INDEX;
        }
        PsiElement grandParent = parent.getParent();
        AtomicInteger previousYamlDocumentCount = new AtomicInteger();
        AtomicBoolean stopTraversal = new AtomicBoolean();
        grandParent.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                if (element.equals(parent)) {
                    stopTraversal.set(true);
                }
                if (stopTraversal.get()) {
                    return;
                }
                if (element instanceof YAMLDocument) {
                    previousYamlDocumentCount.incrementAndGet();
                }
            }
        });
        return previousYamlDocumentCount.get() - 1; // ignore 1st document that corresponds to the Configuration
    }
}
