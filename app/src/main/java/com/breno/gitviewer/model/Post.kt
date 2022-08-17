package com.breno.gitviewer.model

data class Post(
    val name: String,
    val owner: User,
    val html_url: String,
    val description: String?,
    val url: String,
    val contributors_url: String
)