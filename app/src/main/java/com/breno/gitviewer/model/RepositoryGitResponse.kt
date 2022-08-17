package com.breno.gitviewer.model

data class RepositoryGitResponse(
    val description: String?,
    val name: String?,
    val owner: User?,
    val stargazers_count: Int?,
    val watchers_count: Int?,
    val subscribers_count: Int?,
    val updated_at: String?,
    val default_branch: String?,
    val language: String?,
    val contributors_url: String?
)