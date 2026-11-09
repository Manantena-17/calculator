package com.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.model.User
import com.example.app.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewmodel: ViewModel() {
    val userRepository = UserRepository()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    fun getUser(){
        viewModelScope.launch {
            val userFetched=userRepository.fetchUser()
            _user.postValue(userFetched)
        }
    }
}