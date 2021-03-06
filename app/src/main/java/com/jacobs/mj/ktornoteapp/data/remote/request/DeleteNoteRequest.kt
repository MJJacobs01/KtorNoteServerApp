package com.jacobs.mj.ktornoteapp.data.remote.request

/**
 * Created by mj on 2021/03/06 at 8:34 AM
 *
 * Same as on the server to be able to delete a note
 *
 *  It is important that the variables in this class is named exactly the same as on the server packages
 */
data class DeleteNoteRequest(
    val id: String
)
