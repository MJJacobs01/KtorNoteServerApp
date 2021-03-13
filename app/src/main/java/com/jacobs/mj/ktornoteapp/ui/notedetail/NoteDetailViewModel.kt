package com.jacobs.mj.ktornoteapp.ui.notedetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.jacobs.mj.ktornoteapp.respositories.NoteRepository
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Created by mj on 2021/03/06 at 10:08 AM
 */
class NoteDetailViewModel @ViewModelInject constructor(private val repository: NoteRepository) : ViewModel() {

    fun observerNoteById(noteId: String) = repository.observerNoteById(noteId)
}