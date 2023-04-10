
package com.kang.mockfcmpush.util;

import android.content.Context;
import android.view.View;


public class UIUtils {
    private static Context mContext;

    public static Context getContext() {
        if (mContext != null) return mContext;
        throw new NullPointerException("UIUtils should init first");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        UIUtils.mContext = context.getApplicationContext();
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        return DeviceUtils.dip2px(UIUtils.mContext, dip);
    }

    /**
     * pxz转换dip
     */
    public static int px2dip(int px) {
        return DeviceUtils.px2dip(UIUtils.mContext, px);
    }

    public static boolean hasNULLView(View... views) {
        if (views == null) {
            return false;
        }
        int lenght = views.length;
        if (lenght > 0) {
            for (View v : views) {
                if (v == null) {
                    return true;
                }
            }
        }
        return false;
    }

}
