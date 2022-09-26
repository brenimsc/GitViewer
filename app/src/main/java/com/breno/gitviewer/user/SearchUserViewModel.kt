package com.breno.gitviewer.user

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
import com.breno.gitviewer.model.User
import com.breno.gitviewer.service.GitApi
import com.breno.gitviewer.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchUserViewModel(private val service: GitApi) : BaseViewModel() {

    override var error = MutableLiveData<Boolean>()
    override var loading = MutableLiveData<Boolean>()

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(USER_PERFIL, BACK, CLOSE)
    annotation class Navigation


    @StringDef(REQUEST_GET_USER_SUCESS, REQUEST_GET_USER_ERROR)
    annotation class Response


    companion object {
        const val USER_PERFIL = 0
        const val BACK = 1
        const val CLOSE = 2

        const val REQUEST_GET_USER_SUCESS = "RequestGetUserSucess"
        const val REQUEST_GET_USER_ERROR = "RequestGetUserError"
    }

    val adapter: UseAdapter<User> by lazy {
        UseAdapter.getAdapter(R.layout.item_user_list, BR.user) { _, item ->
            user = item
            onClick(USER_PERFIL)
        }
    }

    @Response
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    @Navigation
    private val _navigation = MutableLiveData<Int>()
    val navigation: LiveData<Int> get() = _navigation

    var user: User? = null
        private set

    var queryAfterTextChanged: Job? = null

    private fun onNavigation(@Navigation navigation: Int) {
        _navigation.value = navigation
    }

    fun onClick(@Navigation navigation: Int) {
        when (navigation) {
            USER_PERFIL, BACK, CLOSE -> onNavigation(navigation)
        }
    }

    private fun onResponse(@Response response: String) {
        _response.postValue(response)
    }

    fun onQueryAfterTextChange(query: Editable?) {
        queryAfterTextChanged?.cancel()
        queryAfterTextChanged = viewModelScope.launch {
            delay(1000)
            onLoading()
            query?.toString()?.isNotBlank()?.takeIf { it }?.run {
                executeRequestUser(query.toString())
            }
        }
    }

    override fun setAdapter() {
        adapter.clearAndAddAll(listOf(user ?: return))
    }

    private suspend fun executeRequestUser(query: String?) {
        query?.let {
            try {
                user = service.getUser(query)
                onResponse(REQUEST_GET_USER_SUCESS)
            } catch (e: Exception) {
                Log.e("BRENOL", e.message.toString())
                onResponse(REQUEST_GET_USER_ERROR)
            } finally {
                onStopLoading()
            }
        }

    }
}