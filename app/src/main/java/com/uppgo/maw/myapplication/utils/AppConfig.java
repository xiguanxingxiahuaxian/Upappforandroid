package com.uppgo.maw.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 项目名称：Upapp
 * 类描述：
 * 创建人：maw@neuqsoft.com
 * 创建时间： 2017/9/8 10:43
 * 修改备注
 */
public class AppConfig {
    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    //版本更新工具类
    public static int getVersionCode(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo("com.uppgo.maw.myapplication", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("getCode", e.getMessage());
        }
        return version;
    }

    /**
     * 获取版本的名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionname = "";
        try {
            versionname = context.getPackageManager().getPackageInfo("com.neuqsoft.newggfwmobile", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("getName", e.getMessage());
        }
        return versionname;
    }
}
