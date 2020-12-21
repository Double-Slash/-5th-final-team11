package com.doubleslas.fifith.alcohol.model.repository

import com.doubleslas.fifith.alcohol.model.network.base.*
import com.google.firebase.auth.AuthCredential

class AuthRepository {
    fun completeLogin(credential: AuthCredential): ApiLiveData<Any> {
        val result = MutableApiLiveData<Any>()
        result.value = ApiStatus.Loading

        return result
    }
}