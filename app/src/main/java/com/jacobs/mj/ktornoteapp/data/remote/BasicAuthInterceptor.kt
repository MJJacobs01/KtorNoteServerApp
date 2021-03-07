package com.jacobs.mj.ktornoteapp.data.remote

import com.jacobs.mj.ktornoteapp.other.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by mj on 2021/03/06 at 8:55 AM
 *
 * This class is used to intercept all outgoing requests
 */

class BasicAuthInterceptor : Interceptor {

    //  Starts as null and then we will assign them once the user has signed in successfully
    var email: String? = null
    var password: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //  Do not want to use the interceptor for all requests
        //  Check condition
        if (request.url.encodedPath in IGNORE_AUTH_URLS) {
            //  If the request is /login or /register do not change it before sending it to the server
            return chain.proceed(request)
        }
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", Credentials.basic(email ?: "", password ?: ""))
            .build()
        return chain.proceed(authenticatedRequest)
    }
}