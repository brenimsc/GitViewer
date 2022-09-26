package com.breno.gitviewer.slide.viewmodel

import androidx.annotation.IntDef
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.breno.gitviewer.utils.Constants

class SlideUserListViewModel(private val firstTab: String) : ViewModel() {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(BACK)
    annotation class Navigation

    companion object {
        const val BACK = 0
    }

    val currentItem: Int
        get() {
            return when (firstTab) {
                Constants.FOLLOWING -> Constants.FOLLOWING_NUMBER_PAGE
                else -> Constants.FOLLOWERS_NUMBER_PAGE
            }
        }

    private val _navigation = MutableLiveData<Int>()
    val navigation: LiveData<Int> get() = _navigation

    fun onClick(@Navigation navigation: Int) {
        _navigation.value = navigation
    }

}