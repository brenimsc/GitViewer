package com.breno.gitviewer.follow.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.model.User
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class FollowViewModel(typeRequest: String, val service: GitApi) : ViewModel() {

    val typeRequest = MutableLiveData(typeRequest)
    val users = MutableLiveData<List<User>>()

    var response = MutableLiveData<Int>(0)
        private set

    var user: User? = null
        private set

    fun getUserFollowing(user: String) {
        viewModelScope.launch {
            try {
                users.postValue(service.getUserFollowing(user))
            } catch (e: Exception) {

            }
        }
    }

    fun getUserFollowers(user: String) {
        viewModelScope.launch {
            try {
                users.postValue(service.getUserFollowers(user))
            } catch (e: Exception) {

            }
        }
    }

    fun executeRequestGetUser(login: String) {
        viewModelScope.launch {
            try {
                val result = service.getUser(login)
                user = result
                response.postValue(UserListViewModel.RESPONSE_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
            }
        }
    }


}