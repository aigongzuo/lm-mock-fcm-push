package com.kang.mockfcmpush.concurrent;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * 每个要执行的任务
 */
public abstract class LMTask implements Runnable {
    private ILMTaskMonitor mMonitor;
    @Nullable
    private CountDownLatch countDownLatch;
    private final LinkedList<LMTask> subListTask = new LinkedList<>();

    public LMTask() {
    }

    public LMTask(ILMTaskMonitor monitor) {
        this.mMonitor = monitor;
    }


    public final void run() {
        if (this.mMonitor != null) {
            this.mMonitor.onStart(this);
        }

        try {
            this.runInTryCatch();
            synchronized (subListTask) {
                runSubTask(subListTask);
            }
        } catch (Throwable var5) {
            if (this.mMonitor != null) {
                this.mMonitor.onError(this, var5);
            } else {
                var5.printStackTrace();
            }
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            if (this.mMonitor != null) {
                this.mMonitor.onEnd(this);
            }
        }

    }

    /**
     * 执行子任务
     *
     * @param subListTask 子任务列表
     */
    private void runSubTask(LinkedList<LMTask> subListTask) {
        if (subListTask == null) {
            return;
        }
        while (subListTask.size() > 0) {
            LMTask lmTask = subListTask.poll();
            if (lmTask != null) {
                lmTask.run();
            }
        }
    }

    public abstract void runInTryCatch();

    public void setMonitor(ILMTaskMonitor rm) {
        this.mMonitor = rm;
    }

    public void setCountDownLatch(@Nullable CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void addSubTask(LMTask task) {
        synchronized (subListTask) {
            subListTask.offer(task);
        }
    }

    public String toString() {
        return this.getClass().toString();
    }

}