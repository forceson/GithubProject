package com.android.githubproject.data.remote

import com.android.githubproject.data.entity.Repository
import com.android.githubproject.data.entity.Stargazer
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(ApiEndPoint.URL_REPOS)
    fun getRepos(
        @Path("username") username: String,
        @Query("page") page: Int
    ): Single<List<Repository>>

    @GET(ApiEndPoint.URL_STARGAZERS)
    fun getStargazers(
        @Path("username") username: String,
        @Path("repoName") repoName: String
    ): Single<List<Stargazer>>
}