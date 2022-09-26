package com.breno.gitviewer.extensions

import android.content.Context


fun Context.getString(resString: Int?, vararg arguments: Any?): String {
    return resString?.takeIf { it != 0 }?.run {
        getString(resString, *arguments)
    }.orEmpty()
}