package com.uppgo.maw.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.uppgo.maw.myapplication.utils.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by maw on 2017/6/2.
 */

public class VersionUpdateUtil {


    private static VersionUpdateUtil instances;
    private ProgressDialog m_progressDlg;


    /**
     * 通过此类来更新版本
     */
    public void doNewVersionUpdate(final Context context, final String url, final String m_appNameStr, ProgressDialog progressDlg) {

        m_progressDlg = progressDlg;
        downFile(url, m_appNameStr, context
        );

    }

    /**
     * 提示当前为最新版本
     */
    public void notNewVersionDlgShow(final Context context) {
        int verCode = AppConfig.getVersionCode(context);
        String verName = AppConfig.getVersionName(context);
        String str = "当前版本:" + verName + " Code:" + verCode + ",已是最新版,无需更新!";
        Dialog dialog = new AlertDialog.Builder(context).setTitle("软件更新")
                .setMessage(str)// 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialog.dismiss();

                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

    private void downFile(final String url, final String m_appNameStr, final Context context) {
        m_progressDlg.show();
        new Thread() {
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response;
                try {
                    response = client.newCall(request).execute();
                    ResponseBody entity = response.body();
                    long length = entity.contentLength();
                    m_progressDlg.setMax((int) length);//设置进度条的最大值
                    InputStream is = entity.byteStream();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                m_appNameStr);
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                m_progressDlg.setProgress(count);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    //告诉HANDER已经下载完成了，可以安装了
                    m_progressDlg.cancel();
                    update(m_appNameStr, context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 安装程序
     */
    void update(String m_appNameStr, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断当前系统的SDK版本是否大于23
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",new File(Environment
                            .getExternalStorageDirectory(), m_appNameStr)),
                    "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), m_appNameStr)),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    public static VersionUpdateUtil getInstances() {
        if (null == instances) {
            instances = new VersionUpdateUtil();
        }
        return instances;
    }
  /*  public void setOnDialogClickLister(OnDialogClickListener onDialogClickLister){
        this.onDialogClickListener=onDialogClickLister;
    }
    *//**
     * 提供dialog的点击事件监听
     * 此类应用中，只是添加了 确定按钮的接口
     * 暂不更新事件
     *//*
    public  interface  OnDialogClickListener{
        void click();
        void NotDoUpdate();
    }*/

}
