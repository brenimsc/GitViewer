package com.breno.gitviewer.post.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.model.Event
import com.breno.gitviewer.model.RepositoryGitResponse
import com.breno.gitviewer.model.User
import kotlinx.coroutines.*
import java.lang.Exception

class PostDetailViewModel(repo: String, user: String, private val service: GitApi) : ViewModel() {


    companion object {
        const val USER_PERFIL = 0
        const val USER_LIST_CONTRIBUITORS = 1
        const val USER_LIST_STARGAZERS = 2
        const val USER_LIST_SUBSCRIBERS = 3

        const val REQUEST_USER = 5
        const val REQUEST_USER_OK = 6
    }

    private val coroutine = CoroutineScope(Dispatchers.IO)

    private val _post = MutableLiveData<RepositoryGitResponse>()
    val post: LiveData<RepositoryGitResponse>
    get() = _post

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>>
        get() = _events

    private val _navigation = MutableLiveData<Int>(null)
    val navigation: LiveData<Int> get() = _navigation

    private val _request = MutableLiveData<Int>(null)
    val request: LiveData<Int> get() = _request

    private val _response = MutableLiveData<Int>(null)
    val response: LiveData<Int> get() = _request

    lateinit var user: User
     private set

    val name = MediatorLiveData<String>().apply {
        addSource(post) { value = it.owner?.login}
    }

    init {
        executeRequest(repo, user)
        executeRequestEvents(repo, user)
    }

    private fun executeRequest(repo: String, user: String) {
        coroutine.launch {
            try {
                val result = service.getRepoUser(user, repo)
                _post.postValue(result)

            } catch (e: Exception) {
                //* to do *//
                Log.e("BRENOL", "${e.message} $e")
            }
        }
    }

    private fun executeRequestEvents(repo: String, user: String) {
        coroutine.launch {
            try {
                val result = service.getEvents(user, repo)
                _events.postValue(result)

            } catch (e: Exception) {
                // to do
                Log.e("BRENOL", "${e.message} $e")
            }
        }
    }

    fun executeRequestUser() {
        coroutine.launch {
            post.value?.let {
                try {
                    it.owner?.login?.let { login ->
                        user = service.getUser(login)
                    }
                    _request.postValue(REQUEST_USER_OK)
                } catch (e: Exception) {

                }
            }
        }
    }

    fun onNavigation(navigation: Int) {
        Log.e("BRENOL", "Aqui $navigation")
        _navigation.value = navigation
    }

    fun onRequest(requestId: Int) {
        _request.value = requestId
    }

    override fun onCleared() {
        super.onCleared()
        coroutine.cancel()
    }


}