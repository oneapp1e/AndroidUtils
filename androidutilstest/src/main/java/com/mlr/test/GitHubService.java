package com.mlr.test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mulinrui on 12/12 0012.
 */

public interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(@Path("owner") String owner, @Path("repo") String repo);

}
