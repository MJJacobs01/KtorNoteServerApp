package com.jacobs.mj.ktornoteapp.respositories

import android.app.Application
import com.jacobs.mj.ktornoteapp.data.local.NoteDAO
import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import com.jacobs.mj.ktornoteapp.data.remote.NoteApi
import com.jacobs.mj.ktornoteapp.data.remote.request.AccountRequest
import com.jacobs.mj.ktornoteapp.other.Resource
import com.jacobs.mj.ktornoteapp.other.checkForInternetConnection
import com.jacobs.mj.ktornoteapp.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
                noteDAO.getAllNotes()
            },
            fetch = {
                noteApi.getNotes()
            },
            saveFetchResult = { response ->
                response.body()?.let {
                    insertNotes(it)
                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }

    suspend fun insertNote(note: Note) {
        val response = try {
            noteApi.addNote(note)
        } catch (e: Exception) {
            null
        }
        //Check condition
        if (response != null && response.isSuccessful) {
            //  If the note was added to the server successfully
            noteDAO.insertNote(note.apply { isSynced = true })
        } else {
            //  If the note was not added to the server
            noteDAO.insertNote(note)
        }
    }

    suspend fun insertNotes(notes: List<Note>) {
        //  Inserting all the notes in the list to the database
        notes.forEach { insertNote(it) }
    }

    suspend fun getNoteById(noteId: String) = noteDAO.getNoteById(noteId)
}