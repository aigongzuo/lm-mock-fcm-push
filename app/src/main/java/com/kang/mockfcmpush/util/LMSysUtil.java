package com.kang.mockfcmpush.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;

/**
 * 运行系统相关参数工具类
 */
public class LMSysUtil {

    public static String getProcessName(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        } else {
            return getProcessName(application, getPid());
        }
    }


    public static int getPid() {
        return android.os.Process.myPid();
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 判断是否在主进程
     *
     * @return true：在  false：不在
     */
    private boolean mainProcess(Context context) {
        String processName = getProcessName(context, android.os.Process.myPid());
        return TextUtils.isEmpty(processName) || processName.equals(context.getPackageName());
    }

    /**
     * 获取当前进程名称
     *
     * @param context 上下文
     * @param pid     pid
     * @return 进程名称
     */
    private static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps == null) {
                return "";
            }
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return "";
    }
}
