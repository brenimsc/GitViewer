package com.breno.gitviewer.main.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.model.Post
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(val service: GitApi) : ViewModel() {


    var posts = MutableLiveData<List<Post>>()
        private set

    var title = MutableLiveData<String>("Test")
        private set

    var visible = MutableLiveData<Boolean>()
        private set

    init {
        getPostsApi()
        visible.value = true
    }

    private fun getPostsApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                delay(2000)
                val post = service.getPosts()
                Log.e("BRENOL", "$post \n ${post.items.size}")
                posts.postValue(post.items)
            } catch (e: Exception) {
                Log.e("BRENOL", "$e")
            }
            //posts.value = post.items
        }


        visible.value = false
    }

    fun getPosts(): LiveData<List<Post>> {
        return posts
    }

    val onNavbarClick = MutableLiveData<String>()

    fun onClick() {
        Log.e("Clickkkk", "Clicou aqui")
        onNavbarClick.value = "start action clicked"
    }

    fun stopCoroutine() {
        CoroutineScope(Dispatchers.IO).cancel()
    }


}

