package com.breno.gitviewer.extensions

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.breno.gitviewer.main.ui.MainActivity


fun AppCompatActivity.goActivity(
    activity: Class<*>,
    extras: Bundle
) {
    startActivity(
        Intent(this, activity).apply {
            extras.takeIf { !it.isEmpty }?.run {
                putExtras(extras)
            }
        }
    )
}

fun AppCompatActivity.goActivityWithCondition(
    activity: Class<*>,
    extras: Bundle,
    condition: Boolean? = null,
) {

    condition?.takeIf { it }?.run {
        takeIf { it }?.run {
            startActivity(
                Intent(this@goActivityWithCondition, activity).apply {
                    extras.takeIf { !it.isEmpty }?.run {
                        putExtras(extras)
                    }
                }
            )
        } ?: Toast.makeText(
            this@goActivityWithCondition,
            "Ops! Algo deu errado. Tente novamente mais tarde",
            Toast.LENGTH_SHORT
        ).show()
    }


}

fun Fragment.goActivity(
    activity: Class<*>,
    extras: Bundle
) {
    startActivity(
        Intent(requireContext(), activity).apply {
            extras.takeIf { !it.isEmpty }?.run {
                putExtras(extras)
            }
        }
    )
}

fun AppCompatActivity.binding(binding: ViewDataBinding) {
    binding.apply {
        this.lifecycleOwner = this@binding
    }
}

fun AppCompatActivity.goToLink(url: String?) {
    url?.let {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    } ?: Toast.makeText(this, "Não foi possível realizar essa ação", Toast.LENGTH_SHORT).show()

}

fun AppCompatActivity.backToActivityMain() {
    finish()
    startActivity(Intent(this, MainActivity::class.java).apply {
        addFlags(FLAG_ACTIVITY_CLEAR_TOP)
    })
}

