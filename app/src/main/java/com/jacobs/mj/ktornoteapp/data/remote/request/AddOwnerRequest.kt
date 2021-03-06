package com.jacobs.mj.ktornoteapp.data.remote.request

/**
 * Created by mj on 2021/03/06 at 8:34 AM
 *
 * Same as on the server to be able to add a owner to the note
 */
data class AddOwnerRequest(
    val owner: String,
    val noteId: String
)
