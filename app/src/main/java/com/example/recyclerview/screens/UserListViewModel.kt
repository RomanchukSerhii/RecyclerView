package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserListener
import com.example.recyclerview.model.UsersService

class UserListViewModel(
    private val usersService: UsersService
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        loadUsers()
    }

    fun moveUser(user: User, moveBy: Int) {
        usersService.moveUser(user, moveBy)
    }

    fun deleteUser(user: User) {
        usersService.deleteUser(user)
    }

    fun fireUser(user: User) {
        usersService.fireUser(user)
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    private fun loadUsers() {
        usersService.addListener(listener)
    }

    private val listener: UserListener = {
        _users.value = it
    }
}