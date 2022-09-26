package com.breno.gitviewer.helper

import com.breno.gitviewer.R

object HelperEvents {
    fun returnColor(language: String): Int {
        return when (language) {
            "Kotlin" -> R.color.lilas
            "Shell" -> R.color.black
            "Emacs Lisp" -> R.color.gray_light
            "Python" -> R.color.orange
            "Vim script" -> R.color.black
            "HTML" -> R.color.blue
            "Ruby" -> R.color.red
            "JavaScript" -> R.color.green
            "Java" -> R.color.lilas
            "" -> R.color.gray_dark
            else -> R.color.black
        }
    }
}
