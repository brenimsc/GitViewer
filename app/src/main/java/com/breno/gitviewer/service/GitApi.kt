package com.breno.gitviewer.service

import com.breno.gitviewer.model.Event
import com.breno.gitviewer.model.PostsResponse
import com.breno.gitviewer.model.RepositoryGitResponse
import com.breno.gitviewer.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApi {

    @GET("search/repositories")
    suspend fun getPosts(@Query("q") query: String) : PostsResponse

    @GET("repos/{user}/{endpoint}")
    suspend fun getRepoUser(@Path("user") user: String, @Path("endpoint") endpoint: String) : RepositoryGitResponse

    @GET("repos/{user}/{repo}/events")
    suspend fun getEvents(@Path("user") user: String, @Path("repo") endpoint: String) : List<Event>

    @GET("users/{user}")
    suspend fun getUser(@Path("user") user: String) : User

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


}