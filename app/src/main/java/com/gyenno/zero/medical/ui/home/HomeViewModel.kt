package com.gyenno.zero.medical.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.jpush.android.api.JPushInterface

class HomeViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun setLoading(loading: Boolean) {
        _loading.value = loading
    }
}