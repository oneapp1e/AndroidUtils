package com.mlr.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mlr.utils.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mulinrui on 12/6 0006.
 */
public class Retrofit2Activity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit2);
        Button button = (Button) findViewById(R.id.button);
        final TextView textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GitHubService gitHubService = RetrofitBuilder.retrofit.create(GitHubService.class);
                Call<List<Contributor>> call = gitHubService.repoContributors("oneapp1e", "AndroidUtils");
                call.enqueue(new Callback<List<Contributor>>() {
                    @Override
                    public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                        StringBuilder ss = new StringBuilder();
                        for (int i = 0; i < response.body().size(); i++) {
                            Contributor contributor = response.body().get(i);
                            ss.append(contributor.toString());
                        }
                        textView.setText(ss.toString());
                    }

                    @Override
                    public void onFailure(Call<List<Contributor>> call, Throwable t) {
                        textView.setText("Something went wrong: " + t.getMessage());
                    }
                });

            }
        });
    }

}
