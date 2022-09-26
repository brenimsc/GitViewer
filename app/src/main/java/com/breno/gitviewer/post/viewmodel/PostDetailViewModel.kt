package com.breno.gitviewer.post.viewmodel

import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breno.customadapter.UseAdapter
import com.breno.gitviewer.BR
import com.breno.gitviewer.R
import com.breno.gitviewer.model.Event
import com.breno.gitviewer.model.RepositoryGitResponse
import com.breno.gitviewer.model.User
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PostDetailViewModel(val repo: String, val nameUser: String, private val service: GitApi) :
    BaseViewModel() {


    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        USER_LIST_STARGAZERS,
        USER_LIST_SUBSCRIBERS,
        USER_LOADED,
        RETRY,
        BACK
    )
    annotation class Navigation

    @IntDef(USER_PERFIL)
    annotation class Request

    @StringDef(
        REQUEST_USER_SUCCESS,
        REQUEST_EVENTS_SUCCESS,
        REQUEST_REPO_SUCCESS,
        REQUEST_ERROR,
        REQUEST_EVENTS_ERROR,
        REQUEST_REPO_ERROR,
        REQUEST_USER_ERROR
    )
    annotation class Response

    companion object {
        const val USER_PERFIL = 0
        const val USER_LIST_STARGAZERS = 2
        const val USER_LIST_SUBSCRIBERS = 3
        const val USER_LOADED = 4
        const val RETRY = 5
        const val BACK = 6

        const val REQUEST_USER = 6
        const val REQUEST_EVENTS = 7
        const val REQUEST_USER_SUCCESS = "RequestUserSuccess"
        const val REQUEST_EVENTS_SUCCESS = "RequestEventsSuccess"
        const val REQUEST_REPO_SUCCESS = "RequestRepoSuccess"
        const val REQUEST_USER_ERROR = "RequestUserError"
        const val REQUEST_EVENTS_ERROR = "RequestEventsError"
        const val REQUEST_REPO_ERROR = "RequestRepoError"
        const val REQUEST_ERROR = "RequestError"
    }

    val adapterEvent by lazy {
        UseAdapter.getAdapter<Event>(R.layout.item_events, BR.event)
    }

    private val coroutine = CoroutineScope(Dispatchers.IO)

    private val _repositoryGit = MutableLiveData<RepositoryGitResponse>()
    val repositoryGit: LiveData<RepositoryGitResponse> get() = _repositoryGit

    var events = MutableLiveData<List<Event>>()
        private set

    override var error = MutableLiveData<Boolean>()
    override var loading = MutableLiveData<Boolean>()

    private val _navigation = MutableLiveData<Int>()
    val navigation: LiveData<Int> get() = _navigation

    private val _request = MutableLiveData<Int>()
    val request: LiveData<Int> get() = _request

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    lateinit var user: User
        private set

    init {
        executeRequestRepo(repo, nameUser)
    }

    private fun executeRequestRepo(repo: String, user: String) {
        onLoading()
        coroutine.launch {
            try {
                val result = service.getRepoUser(user, repo)
                _repositoryGit.postValue(result)
                onResponse(REQUEST_REPO_SUCCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", "${e.message} $e")
                onResponse(REQUEST_REPO_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    fun executeRequestEvents(repo: String, user: String) {
        onLoading()
        coroutine.launch {
            try {
                val result = service.getEvents(user, repo)
                events.postValue(result)
                onResponse(REQUEST_EVENTS_SUCCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", "${e.message} $e")
                onResponse(REQUEST_EVENTS_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    fun executeRequestUser() {
        onLoading()
        coroutine.launch {
            repositoryGit.value?.let {
                try {
                    it.owner?.login?.let { login ->
                        user = service.getUser(login)
                        onResponse(REQUEST_USER_SUCCESS)
                    }
                } catch (e: Exception) {
                    Log.e("BRENOL", "${e.message} $e")
                    onResponse(REQUEST_USER_ERROR)
                } finally {
                    onStopLoading()
                }
            }
        }
    }

    fun onClick(@Navigation navigation: Int) {
        when (navigation) {
            USER_PERFIL -> onRequest(REQUEST_USER)
            USER_LIST_STARGAZERS,
            USER_LIST_SUBSCRIBERS,
            BACK-> onNavigation(navigation)
            RETRY -> handleErrorRequest(response.value)
        }
    }

    private fun handleErrorRequest(response: String?) {
        response?.let {
            when (it) {
                REQUEST_REPO_ERROR -> executeRequestRepo(repo, nameUser)
                REQUEST_EVENTS_ERROR -> executeRequestEvents(repo, nameUser)
                REQUEST_USER_ERROR -> executeRequestUser()
                else -> Log.e("BRENOL", "Nada")
            }
        }
    }

    fun onNavigation(@Navigation navigation: Int) {
        _navigation.value = navigation
    }

    fun onRequest(@Request requestId: Int) {
        _request.value = requestId
    }

    private fun onResponse(@Response response: String) {
        _response.postValue(response)
    }

    override fun setAdapter() {
        onStopLoading()
        adapterEvent.clearAndAddAll(events.value ?: arrayListOf())
    }

    override fun onCleared() {
        super.onCleared()
        coroutine.cancel()
    }


}