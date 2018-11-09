package com.github.novotnyr.idea.rabbitmq;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiFileDataContext implements DataContext {
    private final PsiFile psiFile;

    public PsiFileDataContext(PsiFile psiFile) {
        this.psiFile = psiFile;
    }

    @Nullable
    @Override
    public Object getData(String dataId) {
        if (CommonDataKeys.PSI_FILE.is(dataId)) {
            return this.psiFile;
        }
        if (CommonDataKeys.PROJECT.is(dataId)) {
            return this.psiFile.getProject();
        }
        if (CommonDataKeys.EDITOR.is(dataId)) {
            return FileEditorManager.getInstance(this.psiFile.getProject()).getSelectedTextEditor();
        }

        return null;
    }

    @Nullable
    @Override
    public <T> T getData(@NotNull DataKey<T> key) {
        return (T) this.getData(key.getName());
    }
}
