package com.jacobs.mj.ktornoteapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created by mj on 2021/03/06 at 8:05 AM
 *
 * Needs to be the same as on the server
 *
 * 1st class to be created
 */
@Entity(tableName = "notes")
data class Note(
    val title: String,
    val content: String,
    val date: Long,
    val owners: List<String>,
    val color: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    @Expose(deserialize = false, serialize = false)     //  Makes sure that it is ignored when syncing with the server
    val isSynced: Boolean = false
)
