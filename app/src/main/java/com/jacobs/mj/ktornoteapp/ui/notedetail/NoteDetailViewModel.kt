package com.jacobs.mj.ktornoteapp.ui.notedetail

import android.util.EventLog
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacobs.mj.ktornoteapp.other.Event
import com.jacobs.mj.ktornoteapp.other.Resource
import com.jacobs.mj.ktornoteapp.respositories.NoteRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch

/**
 * Created by mj on 2021/03/06 at 10:08 AM
 */
class NoteDetailViewModel @ViewModelInject constructor(private val repository: NoteRepository) : ViewModel() {

    private val _addOwnerStatus = MutableLiveData<Event<Resource<String>>>()
    val addOwnerStatus: LiveData<Event<Resource<String>>> = _addOwnerStatus

    fun observerNoteById(noteId: String) = repository.observerNoteById(noteId)

    fun addOwnerToNote(owner: String, noteId: String) {
        _addOwnerStatus.postValue(Event(Resource.loading(null)))
        if (owner.isEmpty() || noteId.isEmpty()) {
            _addOwnerStatus.postValue(Event(Resource.error("The owner can not be empty", null)))
            return
        }
        viewModelScope.launch {
            val result = repository.addOwnerToNote(owner, noteId)
            _addOwnerStatus.postValue(Event(result))
        }
    }
}