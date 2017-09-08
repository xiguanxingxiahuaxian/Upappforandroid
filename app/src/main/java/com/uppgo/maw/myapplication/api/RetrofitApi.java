package com.uppgo.maw.myapplication.api;

import com.uppgo.maw.myapplication.bean.UpappDTO;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 项目名称：Upapp
 * 类描述：
 * 创建人：maw@neuqsoft.com
 * 创建时间： 2017/9/8 10:15
 * 修改备注
 */
public interface RetrofitApi {
    @GET("/upapp")
    Call<UpappDTO> Update();
}
