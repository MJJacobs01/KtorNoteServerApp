package com.jacobs.mj.ktornoteapp.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacobs.mj.ktornoteapp.other.Resource
import com.jacobs.mj.ktornoteapp.respositories.NoteRepository
import kotlinx.coroutines.launch

/**
 * Created by mj on 2021/03/06 at 10:08 AM
 */
class AuthViewModel @ViewModelInject constructor(private val repository: NoteRepository) : ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<String>>()
    val registerStatus: LiveData<Resource<String>> = _registerStatus

    private val _loginStatus = MutableLiveData<Resource<String>>()
    val loginStatus: LiveData<Resource<String>> = _loginStatus

    //  Have to check length and complexity of password here as well
    fun register(email: String, password: String, repeatedPassword: String) {
        //  When we start this class emit loading status, notify the observers
        _registerStatus.postValue(Resource.loading(null))
        //  Check condition
        if (email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()) {
            //  If any of the fields are empty post error resource
            _registerStatus.postValue(Resource.error("Please fill in all the fields", null))
            return
        }
        //  Check condition
        if (password!=repeatedPassword){
            //  If the passwords does not match
            _registerStatus.postValue(Resource.error("The passwords do not match", null))
            return
        }
        viewModelScope.launch {
            val result = repository.register(email, password)
            _registerStatus.postValue(result)
        }
    }

    fun login(email: String, password: String) {
        //  When we start this class emit loading status, notify the observers
        _loginStatus.postValue(Resource.loading(null))
        //  Check condition
        if (email.isEmpty() || password.isEmpty()) {
            //  If any of the fields are empty post error resource
            _loginStatus.postValue(Resource.error("Please fill in all the fields", null))
            return
        }
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginStatus.postValue(result)
        }
    }
}