package com.jacobs.mj.ktornoteapp.data.remote.response

/**
 * Created by mj on 2021/03/06 at 8:42 AM
 *
 * Needs to be the same as it is on the server
 */
data class SimpleResponse(
    val isSuccessful:Boolean,
    val message:String
)