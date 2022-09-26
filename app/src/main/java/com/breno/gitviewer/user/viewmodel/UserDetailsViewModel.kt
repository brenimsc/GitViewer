package com.breno.gitviewer.user.viewmodel

import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.breno.customadapter.UseAdapter
import com.breno.gitviewer.BR
import com.breno.gitviewer.R
import com.breno.gitviewer.model.RepositoryGitResponse
import com.breno.gitviewer.model.User
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class UserDetailsViewModel(val user: User, private val service: GitApi) : BaseViewModel() {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(USERS_FOLLOWERS, USERS_FOLLOWING, SEARCH_USER, VIEW_GITHUB, RETRY, BACK, CLOSE)
    annotation class Navigation

    @StringDef(REQUEST_GET_USER_SUCESS, REQUEST_GET_USER_ERROR)
    annotation class Response
    companion object {
        const val USERS_FOLLOWING = 10
        const val USERS_FOLLOWERS = 100
        const val SEARCH_USER = 200
        const val VIEW_GITHUB = 300
        const val RETRY = 1000
        const val BACK = 6
        const val CLOSE = 7

        const val REQUEST_GET_USER_SUCESS = "RequestGetUserSucess"
        const val REQUEST_GET_USER_ERROR = "RequestGetUserError"
    }

    var repositories = MutableLiveData<List<RepositoryGitResponse>>()
        private set

    var empty = MutableLiveData(false)

    override var loading = MutableLiveData<Boolean>()

    override var error = MutableLiveData<Boolean>()

    private val _navigation = MutableLiveData<Int>()
    val navigation: LiveData<Int> get() = _navigation

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    val adapter by lazy {
        UseAdapter.getAdapter<RepositoryGitResponse>(
            R.layout.item_user_repositorie,
            BR.userRepository
        )
    }

    init {
        executeRequestRepos()
    }

    private fun executeRequestRepos() {
        onLoading()
        viewModelScope.launch {
            try {
                val result = service.getUserRepos(user.login)
                repositories.value = result
                onResponse(REQUEST_GET_USER_SUCESS)
                repositories.value?.isEmpty().takeIf { it == true }?.run {
                    empty.postValue(true)
                } ?: empty.postValue(false)
            } catch (e: Exception) {
                Log.e("ERRO", e.toString())
                onResponse(REQUEST_GET_USER_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    fun onNavigation(@Navigation navigation: Int) {
        _navigation.value = navigation
    }

    fun onClick(@Navigation navigation: Int) {
        when (navigation) {
            USERS_FOLLOWERS, USERS_FOLLOWING, SEARCH_USER, VIEW_GITHUB, BACK, CLOSE -> onNavigation(navigation)
            RETRY -> executeRequestRepos()
        }
    }

    private fun onResponse(@Response response: String) {
        _response.postValue(response)
    }

    override fun setAdapter() {
        adapter.clearAndAddAll(repositories.value ?: arrayListOf())
    }

}