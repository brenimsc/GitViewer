package com.breno.gitviewer.service

import com.breno.gitviewer.model.*
import io.reactivex.Single
import retrofit2.http.*

interface GitApi {

    @Headers("Username:brenimsc", "Password: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")
    @GET("search/repositories")
    suspend fun getPosts(@Query("q") query: String = "brazil") : PostsResponse

    @Headers("Username:brenimsc", "Password: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")
    @GET("repos/{user}/{endpoint}")
    suspend fun getRepoUser(@Path("user") user: String, @Path("endpoint") endpoint: String) : RepositoryGitResponse

    @Headers("Username:brenimsc", "Password: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")
    @GET("repos/{user}/{repo}/events")
    suspend fun getEvents(@Path("user") user: String, @Path("repo") endpoint: String) : List<Event>

    @Headers("Username:brenimsc", "Password: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")
    @GET("users/{user}")
    suspend fun getUser(@Path("user") user: String) : User

    @Headers("Username:brenimsc", "Password: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")
    @GET("users/{user}/repos")
    suspend fun getUserRepos(@Path("user") user: String) : List<RepositoryGitResponse>

    @GET("repos/{user}/{repo}/stargazers")
    suspend fun getUserStargazers(@Path("user") user: String, @Path("repo") repo: String) : List<User>

    @GET("repos/{user}/{repo}/contributors")
    suspend fun getUserContribuitors(@Path("user") user: String, @Path("repo") repo: String) : List<User>

    @GET("repos/{user}/{repo}/subscribers")
    suspend fun getUserSubscribers(@Path("user") user: String, @Path("repo") repo: String) : List<User>

    @GET("users/{user}/followers")
    suspend fun getUserFollowers(@Path("user") user: String) : List<User>

    @GET("users/{user}/following")
    suspend fun getUserFollowing(@Path("user") user: String) : List<User>

    @Headers("Username:brenimsc", "Password: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")
    @GET("repos/{user}/{endpoint}/contribuitors")
    suspend fun getRepoContribuitors(@Path("user") user: String, @Path("endpoint") endpoint: String) : RepositoryGitResponse


}