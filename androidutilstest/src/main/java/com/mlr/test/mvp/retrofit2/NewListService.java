package com.mlr.test.mvp.retrofit2;

import com.mlr.test.mvp.entity.NewsSummary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 网易新闻列表
 * Created by mulinrui on 12/12 0012.
 */
public interface NewListService {

    /**
     * http://c.m.163.com/nc/article/headline/T1348647853363/0-20.html
     * 头条新闻id T1348647853363
     * 头条接口：http:// c.m.163.com/nc/article/headline/T1348647853363/%d-%d.html
     * @return
     */
    @GET("nc/article/headline/T1348647853363/{start}-{pageSize}.html")
    Call<List<NewsSummary>> getNewHotList(@Path("start") int start, @Path("pageSize") int pageSize);

}
