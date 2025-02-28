package com.pth.androidapp.data.services

import com.pth.androidapp.base.network.BaseRemoteService
import com.pth.androidapp.data.apis.AuthApi
import javax.inject.Inject

class AuthRemoteService @Inject constructor(
    private val authApi: AuthApi
): BaseRemoteService() {

}