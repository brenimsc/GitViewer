package com.breno.gitviewer.user.viewmodel

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.getEndpointListUser
import com.breno.gitviewer.model.User
import kotlinx.coroutines.launch
import java.lang.Exception

class UserListViewModel(titleR: String, val url: String, val service: GitApi) : ViewModel() {

    companion object {
        const val REQUEST_CONTRIBUTORS = 0
        const val REQUEST_STARGAZERS = 1
        const val REQUEST_SUBSCRIBERS = 2

        const val CONTRIBUTORS = "Contributors"
        const val STARGAZERS = "Stargazers"
        const val SUBSCRIBERS = "Subscribers"

        const val RESPONSE_SUCESS = 10
        const val RESPONSE_ERROR = 100
    }

    var title = MutableLiveData(titleR)
        private set

    var resultVisibility = MutableLiveData<Boolean>(false)
        private set

    var listUser = MutableLiveData<List<User>>()
        private set

    var request = MediatorLiveData<Int>().apply {
        addSource(title) { value = verifyTypeRequest() }
    }

    var response = MutableLiveData<Int>(0)
        private set

    var user: User? = null
        private set

    private fun verifyTypeRequest(): Int {
        return when (title.value) {
            CONTRIBUTORS -> REQUEST_CONTRIBUTORS
            STARGAZERS -> REQUEST_STARGAZERS
            SUBSCRIBERS -> REQUEST_SUBSCRIBERS
            else -> -1
        }
    }

    fun executeRequestGetUsersContributors() {
        viewModelScope.launch {
            try {
                url.getEndpointListUser().takeIf { it.isNotEmpty() }?.run {
                    val result = service.getUserContribuitors(this[4], this[5])
                    listUser.postValue(result)
                }

            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
            }
        }
    }

    fun executeRequestGetUsersStargazers() {
        viewModelScope.launch {
            try {
                url.getEndpointListUser().takeIf { it.isNotEmpty() }?.run {
                    val result = service.getUserContribuitors(this[4], this[5])
                    listUser.postValue(result)
                }

            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
            }
        }
    }

    fun executeRequestGetUsersSubscribers() {
        viewModelScope.launch {
            try {
                url.getEndpointListUser().takeIf { it.isNotEmpty() }?.run {
                    val result = service.getUserSubscribers(this[4], this[5])
                    listUser.postValue(result)
                }

            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
            }
        }
    }

    fun executeRequestGetUser(login: String) {
        viewModelScope.launch {
            try {
                val result = service.getUser(login)
                user = result
                response.postValue(RESPONSE_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
            }
        }
    }


}