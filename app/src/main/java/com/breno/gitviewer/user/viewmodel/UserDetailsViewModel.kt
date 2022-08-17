package com.breno.gitviewer.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.model.RepositoryGitResponse
import com.breno.gitviewer.model.User
import kotlinx.coroutines.launch
import java.lang.Exception

class UserDetailsViewModel(val user: User, val service: GitApi) : ViewModel() {

    companion object {
        const val USERS_FOLLOWING = 10
        const val USERS_FOLLOWERS = 100
    }

    private val _repositories = MutableLiveData<List<RepositoryGitResponse>>()
    val repositories: LiveData<List<RepositoryGitResponse>> get() = _repositories

    var navigation = MutableLiveData<Int>(0)
        private set

    init {
        executeRequestRepos()
    }

    private fun executeRequestRepos() {
        viewModelScope.launch {
            try {
                val result = service.getUserRepos(user.login)
                _repositories.postValue(result)
            } catch (e: Exception) {
                Log.e("ERRO", e.toString())
            }
        }
    }

    fun onNavigation(navigationId: Int) {
        navigation.value = navigationId
    }

}