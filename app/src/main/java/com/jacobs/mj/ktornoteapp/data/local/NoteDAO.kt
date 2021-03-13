package com.jacobs.mj.ktornoteapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jacobs.mj.ktornoteapp.data.local.entities.LocallyDeleteNoteId
import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import kotlinx.coroutines.flow.Flow

/**
 * Created by mj on 2021/03/06 at 8:16 AM
 *
 * 3rd class to be created
 */
@Dao
interface NoteDAO {

    //  With OnConflictStrategy.REPLACE we are rather replacing a note than updating it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("delete from notes where id = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("delete from notes where isSynced = 1")          //  1 is where boolean value is true
    suspend fun deleteAllSyncedNotes()

    @Query("select * from notes where id = :noteId")
    fun observeNoteById(noteId: String): LiveData<Note>

    @Query("select * from notes where id = :noteId")
    suspend fun getNoteById(noteId: String): Note?

    @Query("select * from notes order by date desc")
    fun getAllNotes(): Flow<List<Note>>

    @Query("select * from notes where isSynced = 0")        //  0 is where boolean value is false
    suspend fun getAllUnSyncedNotes(): List<Note>

    @Query("select * from locallydeletenoteid")
    suspend fun getAllLocallyDeletedNoteIds(): List<LocallyDeleteNoteId>

    @Query("delete from locallydeletenoteid where deletedNoteId = :deleteNoteId")
    suspend fun deleteLocallyDeleteNoteId(deleteNoteId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocallyDeletedNoteId(locallyDeleteNoteId: LocallyDeleteNoteId)

    @Query("delete from notes")
    suspend fun deleteAllNotes()
}