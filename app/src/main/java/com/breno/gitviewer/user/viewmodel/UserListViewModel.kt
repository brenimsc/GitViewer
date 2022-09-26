package com.breno.gitviewer.user.viewmodel

import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.breno.customadapter.UseAdapter
import com.breno.gitviewer.BR
import com.breno.gitviewer.R
import com.breno.gitviewer.extensions.getEndpointListUser
import com.breno.gitviewer.model.User
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserListViewModel(val type: String, private val url: String, private val service: GitApi) :
    BaseViewModel() {

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        RESPONSE_GET_USER_SUCESS,
        RESPONSE_ERROR,
        RESPONSE_LIST_SUCESS,
        RESPONSE_LIST_USER_ERROR,
        RESPONSE_GET_USER_ERROR
    )
    annotation class Response

    @IntDef(REQUEST_STARGAZERS, REQUEST_SUBSCRIBERS, REQUEST_USER)
    annotation class Request

    @IntDef(USER_PERFIL, BACK, CLOSE)
    annotation class Navigation

    companion object {
        const val REQUEST_STARGAZERS = 1
        const val REQUEST_SUBSCRIBERS = 2
        const val REQUEST_USER = 6


        const val CONTRIBUTORS = "Contributors"
        const val STARGAZERS = "Stargazers"
        const val SUBSCRIBERS = "Subscribers"

        const val USER_PERFIL = 4
        const val RETRY = 5
        const val BACK = 6
        const val CLOSE = 7

        const val RESPONSE_GET_USER_SUCESS = "ResponseGetUserSucess"
        const val RESPONSE_LIST_SUCESS = "ResponseListSucess"
        const val RESPONSE_ERROR = "ResponseError"
        const val RESPONSE_GET_USER_ERROR = "ResponseGetUserError"
        const val RESPONSE_LIST_USER_ERROR = "ResponseListUserError"
    }

    val typeTitle: MutableLiveData<String>
        get() =
            MutableLiveData(
                when (type) {
                    // TODO: Substiuir title por um recurso de string STARGAZERS -> Favortios / SUBSCRIBERS -> Inscritos
                    STARGAZERS -> STARGAZERS
                    SUBSCRIBERS -> SUBSCRIBERS
                    else -> ""
                }
            )


    var listUser = MutableLiveData<List<User>>()
        private set

    var request = MediatorLiveData<Int>().apply {
        addSource(typeTitle) { value = verifyTypeRequest() }
    }
        private set

    var empty = MutableLiveData(false)
        private set

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    private val _navigation = MutableLiveData<Int>()
    val navigation: LiveData<Int> = _navigation

    var user: User? = null
        private set

    val adapter: UseAdapter<User> by lazy {
        UseAdapter.getAdapter(R.layout.item_user_list, BR.user) { _, item ->
            user = item
            onRequest(REQUEST_USER)
            //onNavigation(USER_PERFIL)
        }
    }

    private fun verifyTypeRequest(): Int {
        return when (typeTitle.value) {
            STARGAZERS -> REQUEST_STARGAZERS
            SUBSCRIBERS -> REQUEST_SUBSCRIBERS
            else -> REQUEST_SUBSCRIBERS
        }
    }

    private fun onRequest(@Request request: Int) {
        this.request.value = request
    }

    fun onClick(@Navigation navigation: Int) {
        when (navigation) {
            USER_PERFIL, BACK, CLOSE -> onNavigation(navigation)
            RETRY -> handleRequestError()
        }
    }

    private fun handleRequestError() {
        when (response.value) {
            RESPONSE_ERROR -> { /*do nothing*/
            }
            RESPONSE_GET_USER_ERROR -> onRequest(REQUEST_USER)
            RESPONSE_LIST_USER_ERROR -> onRequest(verifyTypeRequest())
        }
    }

    fun executeRequestGetUsersStargazers() {
        onLoading()
        viewModelScope.launch {
            try {
                url.getEndpointListUser().takeIf { it.isNotEmpty() }?.run {
                    delay(1000)
                    val result = service.getUserStargazers(this[4], this[5])
                    listUser.postValue(result)
                    onResponse(RESPONSE_LIST_SUCESS)
                }
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
                onResponse(RESPONSE_LIST_USER_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    fun executeRequestGetUsersSubscribers() {
        onLoading()
        viewModelScope.launch {
            try {
                url.getEndpointListUser().takeIf { it.isNotEmpty() }?.run {
                    delay(1000)
                    val result = service.getUserSubscribers(this[4], this[5])
                    listUser.postValue(result)
                    onResponse(RESPONSE_LIST_SUCESS)
                }
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
                onResponse(RESPONSE_LIST_USER_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    fun executeRequestGetUser(login: String) {
        onLoading()
        viewModelScope.launch {
            try {
                val result = service.getUser(login)
                user = result
                onResponse(RESPONSE_GET_USER_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.toString())
                onResponse(RESPONSE_GET_USER_ERROR)
            } finally {
                onStopLoading()
            }
        }
    }

    private fun onResponse(@Response response: String) {
        _response.postValue(response)
    }

    fun onNavigation(@Navigation navigation: Int) {
        _navigation.value = navigation
    }

    override fun setAdapter() {
        adapter.clearAndAddAll(listUser.value ?: arrayListOf())
        applyListEmpty()
    }

    private fun applyListEmpty() {
        listUser.value?.isEmpty()?.takeIf { it }?.run {
            empty.postValue(true)
        } ?: empty.postValue(false)
    }

    override var error = MutableLiveData<Boolean>()

    override var loading = MutableLiveData<Boolean>()


}