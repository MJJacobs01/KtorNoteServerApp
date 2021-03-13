package com.jacobs.mj.ktornoteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jacobs.mj.ktornoteapp.data.local.entities.LocallyDeleteNoteId
import com.jacobs.mj.ktornoteapp.data.local.entities.Note

/**
 * Created by mj on 2021/03/06 at 8:28 AM
 *
 * 4th class to be created
 */
@Database(
    entities = [Note::class, LocallyDeleteNoteId::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDAO(): NoteDAO
}