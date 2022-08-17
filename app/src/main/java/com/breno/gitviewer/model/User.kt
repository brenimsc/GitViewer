package com.breno.gitviewer.model

import java.io.Serializable

data class User(
    val login: String,
    val avatar_url: String,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val following_url: String,
    val repos_url: String,
    val name: String?,
    val email: String?,
    val bio: String?,
    val twitter_username: String?,
    val public_repos: String?,
    val followers: String?,
    val following: String?,
    val created_at: String?,
) : Serializable
