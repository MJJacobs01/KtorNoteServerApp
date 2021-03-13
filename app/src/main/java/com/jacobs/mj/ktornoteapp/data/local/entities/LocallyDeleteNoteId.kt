package com.jacobs.mj.ktornoteapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by mj on 2021/03/13 at 4:43 PM
 */
@Entity
data class LocallyDeleteNoteId(
    @PrimaryKey(autoGenerate = false)
    val deletedNoteId: String
)
