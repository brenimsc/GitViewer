package com.breno.gitviewer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract var error: MutableLiveData<Boolean>

    open fun setErrorEnabled() {
        error.postValue(true)
    }

    open fun setErrorDisabled() {
        error.postValue(false)
    }

    abstract var loading: MutableLiveData<Boolean>

    open fun onLoading() {
        setErrorDisabled()
        loading.postValue(true)
    }

    open fun onStopLoading() {
        loading.postValue(false)
    }

    override fun onCleared() {}

    open fun setAdapter() {}


}
