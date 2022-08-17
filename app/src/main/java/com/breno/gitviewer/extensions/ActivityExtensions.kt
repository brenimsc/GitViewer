package com.breno.gitviewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.breno.gitviewer.model.User
import com.breno.gitviewer.user.ui.UserListActivity
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf

fun <T> AppCompatActivity.goActivity(activity: Class<T>, nameParams: String? = null, params: Any? = null) {
    startActivity(
        Intent(this, activity).apply {
            takeIf { params != null }.run {
                when (params) {
                    is String -> {
                        putExtra(nameParams, params)
                    }
                    is Boolean -> {
                        putExtra(nameParams, params)
                    }
                    is User -> {
                        putExtra(nameParams, params)
                    }
                }
            }
        }

    )
}

fun AppCompatActivity.goActivityParams(
    activity: Class<UserListActivity>,
    parametersOf: DefinitionParameters
) {
    startActivity(
        Intent(this, activity).apply {
            parametersOf(parametersOf)
        }

    )
}