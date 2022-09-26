package com.breno.gitviewer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Actor(
    val display_login: String
) : Parcelable
