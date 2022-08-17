package com.breno.gitviewer

fun String.getEndpoint(): String {
    return takeIf { this.contains(BASE_URL) }?.run {
        this.substring(36, this.length)
    } ?: orEmpty()

    //https://api.github.com/repos/lerrua/remote-jobs-brazil

    //"https://api.github.com/users/lerrua"

}

fun String.getEndpointListUser(): MutableList<String> {
    return takeIf { contains(BASE_URL) }?.run {
        split("/").toMutableList()
    } ?: mutableListOf()

}