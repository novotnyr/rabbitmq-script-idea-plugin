package com.github.novotnyr.idea.rabbitmq;

import com.intellij.execution.KillableProcess;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.concurrency.AppExecutorUtil;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract class CallableProcessHandler extends ProcessHandler implements KillableProcess {
    private final Logger LOG = Logger.getInstance("#com.github.novotnyr.idea.rabbitmq.SimpleProcessHandler");

    public static final Void NOTHING = null;

    private Future<Void> taskFuture;

    @Override
    public void startNotify() {
        Callable<Void> task = new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    return doCall();
                } catch (Exception e) {
                    return handleException(e);
                }
            };
        };
        this.taskFuture = AppExecutorUtil.getAppExecutorService().submit(task);
        super.startNotify();
    }

    protected abstract Void doCall();

    /**
     * Handles an exception thrown from {@link #doCall()}. The code
     * runs on non-EDT thread.
     */
    protected abstract Void handleException(Exception e);

    @Override
    protected void destroyProcessImpl() {
        LOG.info("destroyProcess");
        this.taskFuture.cancel(true);
    }

    @Override
    protected void detachProcessImpl() {
        LOG.info("detachProcess");
    }

    @Override
    public boolean detachIsDefault() {
        return false;
    }

    @Nullable
    @Override
    public OutputStream getProcessInput() {
        return null;
    }

    @Override
    public boolean canKillProcess() {
        return true;
    }

    @Override
    public void killProcess() {
        LOG.info("killProcess");
    }
}
