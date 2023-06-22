package com.example.tf2servers.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataModel: ViewModel() {
    val info: MutableLiveData<ServerInfo> by lazy {
        MutableLiveData<ServerInfo>()
    }
}