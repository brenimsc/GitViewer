package com.breno.gitviewer.extensions

import com.breno.gitviewer.di.BASE_URL

fun String.getEndpointListUser(): MutableList<String> {
    return takeIf { contains(BASE_URL) }?.run {
        split("/").toMutableList()
    } ?: mutableListOf()

}