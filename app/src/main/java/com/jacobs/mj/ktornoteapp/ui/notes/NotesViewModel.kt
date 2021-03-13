package com.jacobs.mj.ktornoteapp.ui.notes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jacobs.mj.ktornoteapp.data.local.entities.LocallyDeleteNoteId
import com.jacobs.mj.ktornoteapp.data.local.entities.Note
import com.jacobs.mj.ktornoteapp.other.Event
import com.jacobs.mj.ktornoteapp.other.Resource
import com.jacobs.mj.ktornoteapp.respositories.NoteRepository
import kotlinx.coroutines.launch

/**
 * Created by mj on 2021/03/06 at 10:08 AM
 */
class NotesViewModel @ViewModelInject constructor(private val repository: NoteRepository) : ViewModel() {
    //  To make sure that we do not update unnecessary
    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _allNotes = _forceUpdate.switchMap {
        repository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }
    val allNotes: LiveData<Event<Resource<List<Note>>>> = _allNotes

    fun syncAllNotes() = _forceUpdate.postValue(true)

    fun deleteLocallyDeletedNoteId(deleteNoteId: String) = viewModelScope.launch {
        repository.deleteLocallyDeletedNoteId(deleteNoteId)
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun deleteNote(noteId: String) = viewModelScope.launch {
        repository.deleteNote(noteId)
    }
}