package com.jacobs.mj.ktornoteapp.data.remote.request

/**
 * Created by mj on 2021/03/06 at 8:34 AM
 *
 * Same as on the server to be able to create a new account or to log in the user
 */
data class AccountRequest(
    val email: String,
    val password: String
)
