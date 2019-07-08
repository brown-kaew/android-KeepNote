package com.brown.kaew.keepnote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _toastText = MutableLiveData<String>()
    val toastText = _toastText as LiveData<String>

    fun onAddNote() {
        Log.i("${javaClass.simpleName}", "onAddNote()")
        _toastText.value = "onAddNote()"

    }
}