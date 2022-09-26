package com.breno.gitviewer.follow.viewmodel

import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.breno.customadapter.UseAdapter
import com.breno.gitviewer.BR
import com.breno.gitviewer.R
import com.breno.gitviewer.model.User
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FollowViewModel(
    typeRequest: String, private val service: GitApi,
) : BaseViewModel() {


    @Retention(AnnotationRetention.SOURCE)
    @IntDef(USER_PERFIL)
    annotation class Navigation

    @StringDef(
        RESPONSE_GET_USER_FOLLOWERS_SUCESS,
        RESPONSE_GET_USER_FOLLOWING_SUCESS,
        RESPONSE_GET_USER_SUCESS,
        RESPONSE_ERROR
    )
    annotation class Response
    companion object {
        const val RESPONSE_GET_USER_SUCESS = "ResponseGetUserSucess"
        const val RESPONSE_GET_USER_FOLLOWERS_SUCESS = "ResponseGetUserFollowersSucess"
        const val RESPONSE_GET_USER_FOLLOWING_SUCESS = "ResponseGetUserFollowingSucess"
        const val RESPONSE_ERROR = "ResponseError"

        const val USER_PERFIL = 0
    }

    val request = MutableLiveData(typeRequest)
    var users = MutableLiveData<List<User>>(arrayListOf())
        private set

    private val _response = MutableLiveData<String>()
    val response get() = _response

    private val _navigation = MutableLiveData<Int>()
    val navigation get() = _navigation

    var user: User? = null
        private set

    var empty = MutableLiveData(false)
        private set

    val adapter: UseAdapter<User> by lazy {
        UseAdapter.getAdapter(R.layout.item_user_list, BR.user, users.value ?: arrayListOf()) { _, item ->
            executeRequestGetUser(item.login)
        }
    }

    fun getUserFollowing(user: String) {
        viewModelScope.launch {
            try {
                delay(1500)
                users.value = service.getUserFollowing(user)
                onResponse(RESPONSE_GET_USER_FOLLOWING_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
                onResponse(RESPONSE_ERROR)
            }
        }
    }

    fun getUserFollowers(user: String) {
        viewModelScope.launch {
            try {
                delay(1500)
                users.value = service.getUserFollowers(user)
                onResponse(RESPONSE_GET_USER_FOLLOWERS_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
                onResponse(RESPONSE_ERROR)
            }
        }
    }

    private fun executeRequestGetUser(login: String) {
        viewModelScope.launch {
            try {
                val result = service.getUser(login)
                user = result
                onResponse(RESPONSE_GET_USER_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
                onResponse(RESPONSE_ERROR)
            }
        }
    }

    fun onNavigation(@Navigation navigation: Int) {
        _navigation.value = navigation
    }

    private fun onResponse(@Response response: String) {
        _response.postValue(response)
    }

    override fun setAdapter() {
        adapter.clearAndAddAll(users.value ?: arrayListOf())
        applyEmptyUsers()
    }

    private fun applyEmptyUsers() {
        users.value?.isEmpty()?.takeIf { it }?.run {
            empty.postValue(true)
        } ?: empty.postValue(false)
    }

    override var error = MutableLiveData<Boolean>()

    override var loading = MutableLiveData<Boolean>()


}