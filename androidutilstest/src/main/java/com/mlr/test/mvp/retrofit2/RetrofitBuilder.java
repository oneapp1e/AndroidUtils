package com.mlr.test.mvp.retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mulinrui on 12/12 0012.
 */
public class RetrofitBuilder {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://c.m.163.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
