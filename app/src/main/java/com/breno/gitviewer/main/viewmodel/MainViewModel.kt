package com.breno.gitviewer.main.viewmodel

import android.text.Editable
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.breno.customadapter.UseAdapter
import com.breno.gitviewer.BR
import com.breno.gitviewer.R
import com.breno.gitviewer.model.Post
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.viewmodel.BaseViewModel
import kotlinx.coroutines.*

class MainViewModel(private val service: GitApi) : BaseViewModel() {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(POST_SELECTED, RETRY)
    annotation class Navigation

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(REQUEST_POST_SUCCESS)
    annotation class Response

    companion object {
        const val POST_SELECTED = 1
        const val RETRY = 2
        const val REQUEST_POST_ERROR = "RequestPostsError"
        const val REQUEST_POST_SUCCESS = "RequestPostsSucess"
    }

    @Navigation
    private val _navigation = MutableLiveData<Int>()
    val navigation: LiveData<Int> get() = _navigation

    @Response
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response


    var posts = MutableLiveData<List<Post>>()
    private set

    override var loading = MutableLiveData<Boolean>()

    override var error = MutableLiveData<Boolean>()

    lateinit var postSelected: Post
        private set

    var queryRepository = MutableLiveData<String>()
        private set

    var queryTextChangedJob: Job? = null


    fun onQueryAfterTextChange(query: Editable?) {
        queryTextChangedJob?.cancel()
        queryTextChangedJob = viewModelScope.launch {
            delay(1000)
            query.toString().isBlank().takeIf { it }?.run {
                getPostsApi(null)
            } ?: getPostsApi(query.toString())
        }
    }



    val adapter: UseAdapter<Post> by lazy {
        UseAdapter.getAdapter(R.layout.item_posts, BR.post) { _, item ->
            postSelected = item
            onClick(POST_SELECTED)
        }
    }

    fun onClick(@Navigation clickAction: Int) {
        when (clickAction) {
            RETRY -> getPostsApi()
            POST_SELECTED -> onNavigation(POST_SELECTED)
        }
    }


    init {
        getPostsApi()
    }

    fun getPostsApi(query: String? = null) {
        onLoading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                delay(2000)
                val post = service.getPosts(query ?: "brazil")
                posts.postValue(post.items)
                onResponse(REQUEST_POST_SUCCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", "$e")
                onResponse(REQUEST_POST_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    private fun onNavigation(@Navigation navigation: Int) {
        _navigation.postValue(navigation)
    }

    private fun onResponse(@Response response: String) {
        _response.postValue(response)
    }

    private fun stopCoroutine() {
        CoroutineScope(Dispatchers.IO).cancel()
    }

    override fun onCleared() {
        stopCoroutine()
    }

    override fun setAdapter() {
        adapter.clearAndAddAll(posts.value ?: arrayListOf())
    }

}

