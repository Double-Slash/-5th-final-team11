package com.doubleslas.fifith.alcohol.model.network.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData


typealias ApiLiveData<T> = LiveData<ApiStatus<T>>
typealias MutableApiLiveData<T> = MutableLiveData<ApiStatus<T>>
typealias MediatorApiLiveData<T> = MediatorLiveData<ApiStatus<T>>