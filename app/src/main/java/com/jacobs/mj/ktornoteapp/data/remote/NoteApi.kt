package com.jacobs.mj.ktornoteapp.data.remote

import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import com.jacobs.mj.ktornoteapp.data.remote.request.AccountRequest
import com.jacobs.mj.ktornoteapp.data.remote.request.AddOwnerRequest
import com.jacobs.mj.ktornoteapp.data.remote.request.DeleteNoteRequest
import com.jacobs.mj.ktornoteapp.data.remote.response.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by mj on 2021/03/06 at 8:44 AM
 */
interface NoteApi {

    //  Sending this request to the register endpoint of our KtorNoteServer
    @POST("/register")
    suspend fun register(@Body registerRequest: AccountRequest): Response<SimpleResponse>       //  @Body let's retrofit know to convert to Json in our request to the server

    //  Sending this request to the login endpoint of our KtorNoteServer
    @POST("/login")
    suspend fun login(@Body loginRequest: AccountRequest): Response<SimpleResponse>             //  @Body let's retrofit know to convert to Json in our request to the server

    //  Sending this request to the addNote endpoint of our KtorNoteServer
    @POST("/addNote")
    suspend fun addNote(@Body note: Note): Response<ResponseBody>                                //  ResponseBody is used if there is no return message from the server

    //  Sending this request to the deleteNote endpoint of our KtorNoteServer
    @POST("/deleteNote")
    suspend fun deleteNote(@Body deleteNoteRequest: DeleteNoteRequest): Response<ResponseBody>

    //  Sending this request to the addOwnerToNote endpoint of our KtorNoteServer
    @POST("/addOwnerToNote")
    suspend fun addOwnerToNote(@Body addOwnerRequest: AddOwnerRequest): Response<SimpleResponse>

    //  Sending this request to the getNotes endpoint of our KtorNoteServer
    @GET("/getNotes")
    suspend fun getNotes(): Response<List<Note>>
}