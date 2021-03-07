package com.jacobs.mj.ktornoteapp.respositories

import android.app.Application
import com.jacobs.mj.ktornoteapp.data.local.NoteDAO
import com.jacobs.mj.ktornoteapp.data.remote.NoteApi
import com.jacobs.mj.ktornoteapp.data.remote.request.AccountRequest
import com.jacobs.mj.ktornoteapp.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mj on 2021/03/06 at 10:11 AM
 */
class NoteRepository @Inject constructor(private val noteDAO: NoteDAO, private val noteApi: NoteApi, private val context: Application) {

    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            //  If everything goes well
            val response = noteApi.register(AccountRequest(email, password))
            //  Check condition
            if (response.isSuccessful && response.body()!!.isSuccessful) {
                //  If the response is successful
                Resource.success(response.body()?.message)       //  .body() is the response from the server
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the server. Check your internet connection!", null)
        }
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            //  If everything goes well
            val response = noteApi.login(AccountRequest(email, password))
            //  Check condition
            if (response.isSuccessful && response.body()!!.isSuccessful) {
                //  If the response is successful
                Resource.success(response.body()?.message)       //  .body() is the response from the server
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the server. Check your internet connection!", null)
        }
    }
}