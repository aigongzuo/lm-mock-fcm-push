package com.kang.mockfcmpush.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kang.mockfcmpush.concurrent.LMTask;

public class LMThreadUtil {
    @NonNull
    private static SafeHander sUi = new SafeHander(Looper.getMainLooper());
    @Nullable
    private static SafeHander sWork = null;
    //工作线程池-之后推广使用
//    private static LMThreadPool mLMThreadPool = new LMThreadPool();

    private LMThreadUtil() {
    }

    /**
     * UI线程执行
     *
     * @param task 具体执行的内容
     */
    public static void runUIThread(@Nullable LMTask task) {
        if (task != null) {
            sUi.post(task);
        }
    }

    /**
     * UI线程延迟执行
     *
     * @param task        具体执行的内容
     * @param delayMillis 1000 = 1秒
     */
    @Nullable
    public static void runUIThreadDelayed(@Nullable LMTask task, long delayMillis) {
        if (task != null) {
            sUi.postDelayed(task, delayMillis);
        }
    }


    /**
     * 当前是否是UI线程
     */
    public static boolean isUIThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * 主线空闲时执行
     *
     * @param task
     */
    public static void runUIThreadWhenIdle(LMTask task) {
        if (task == null) {
            return;
        }
        MessageQueue.IdleHandler idleHandler = () -> {
            task.run();
            return false;
        };
        if (isUIThread()) {
            Looper.myQueue().addIdleHandler(idleHandler);
        } else {
            runUIThread(new LMTask() {
                @Override
                public void runInTryCatch() {
                    Looper.myQueue().addIdleHandler(idleHandler);
                }
            });
        }
    }

    /**
     * 工作线程池队列执行
     *
     * @param task 执行体
     */
    public static void runWorkThread(@Nullable final LMTask task) {
        if (task == null) {
            return;
        }
        //mLMThreadPool.execute(task);
//        BackgroundThreadPool.executeIoTask(task);
        getWorlkHandler().post(task);
    }

    /**
     * 获取工作Handler
     */
    @NonNull
    private static SafeHander getWorlkHandler() {
        if (sWork == null) {
            synchronized (LMThreadUtil.class) {
                if (sWork == null) {
                    HandlerThread ht = new HandlerThread("worlk-thread");
                    ht.start();
                    sWork = new SafeHander(ht.getLooper());
                }
            }
        }
        return sWork;
    }

    /**
     * 工作线程池队列延迟执行
     *
     * @param task        执行体
     * @param delayMillis 1000 = 1秒
     */
    public static void runWorkThreadDelayed(@Nullable LMTask task, long delayMillis) {
        if (task == null) {
            return;
        }
        getWorlkHandler().postDelayed(task, delayMillis);
    }

    private static class SafeHander extends Handler {
        public SafeHander(Looper looper) {
            super(looper);
        }

        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception var3) {
                Log.e("SafeHander", "exception! you should check your code", var3);
                var3.printStackTrace();
            }
        }
    }
}

