package com.uppgo.maw.myapplication.utils;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.uppgo.maw.myapplication.R;
import com.uppgo.maw.myapplication.VersionUpdateUtil;
import com.uppgo.maw.myapplication.adapter.Updataadapter;
import com.uppgo.maw.myapplication.api.BaseUrl;
import com.uppgo.maw.myapplication.api.RetrofitApi;
import com.uppgo.maw.myapplication.bean.UpappDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int m_newVerCode;
    private String m_newVerName;
    private List<String> data;
    private Handler m_mainHandler;
    private ProgressDialog m_progressDlg;
    private AlertDialog dialog;
    private String url;
    private static final int PERMISSION_STATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        //使用retrofit 完成网络请求
        Retrofit retrofit =new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseUrl.URL).build();
        Call<UpappDTO> call = retrofit.create(RetrofitApi.class).Update();
        call.enqueue(new Callback<UpappDTO>() {
            @Override
            public void onResponse(Call<UpappDTO> call, Response<UpappDTO> response) {
                Log.i("MainActivity",response.body().getUrl());

                m_newVerName = response.body().getVername();
                m_newVerCode = response.body().getVercode();
                url=response.body().getUrl();
                data= response.body().getContent();
                if (m_newVerName != null) {
                    initVariable();
                    new checkNewestVersionAsyncTask().execute();
                }
            }

            @Override
            public void onFailure(Call<UpappDTO> call, Throwable t) {

            }
        });
    }

    private void initVariable() {
        m_mainHandler = new Handler();
        m_progressDlg = new ProgressDialog(this);
        m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        m_progressDlg.setIndeterminate(false);
    }

    class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            int vercode = AppConfig.getVersionCode(getApplicationContext());// 用到前面第一节写的方法
            if (m_newVerCode > vercode) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            if (result) {//如果有最新版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断当前系统的SDK版本是否大于23
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STATE);
                }else{
                    ShowDialog();
                }
            } else {
                ShowNoUpdate();
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
    }
    private void ShowDialog() {
        int verCode = AppConfig.getVersionCode(getApplicationContext());
        String verName = AppConfig.getVersionName(getApplicationContext());
        String str = "当前版本：" + verName + " Code:" + verCode + " ,发现新版本：" + m_newVerName +
                " Code:" + m_newVerCode + " ,是否更新？";
        View view = View.inflate(MainActivity.this, R.layout.upappitem, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this).setView(view);
        ListView listView = (ListView) view.findViewById(R.id.lv);
        TextView currentcode = (TextView) view.findViewById(R.id.currentcode);
        TextView oldcode = (TextView) view.findViewById(R.id.oldcode);
        currentcode.setText(m_newVerCode+"");
        oldcode.setText(verCode+"");
        TextView up = (TextView) view.findViewById(R.id.up);
        TextView no = (TextView) view.findViewById(R.id.no);
        TextView yes = (TextView) view.findViewById(R.id.yes);
        Updataadapter adapter= new Updataadapter(data,MainActivity.this);
        listView.setAdapter(adapter);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("upapp","no");
                dialog.dismiss();
                finish();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("upapp","yes");
                m_progressDlg.setTitle("正在下载");
                m_progressDlg.setMessage("请稍候...");
                VersionUpdateUtil.getInstances().doNewVersionUpdate(MainActivity.this
                        , url, m_newVerName, m_progressDlg);
            }
        });
        dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // 显示对话框
        dialog.show();
    }
    private void ShowNoUpdate() {
        int verCode = AppConfig.getVersionCode(this);
        String verName = AppConfig.getVersionName(this);
        String str = "当前版本:" + verName + " Code:" + verCode + ",已是最新版,无需更新!";
        Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新")
                .setMessage(str)// 设置内容
                .setPositiveButton("确定",// 设置确定按钮
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).create();// 创建
        // 显示对话框
        dialog.show();
    }

}
