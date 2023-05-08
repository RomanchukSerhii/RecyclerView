package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.UserNotFoundException
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserDetails
import com.example.recyclerview.model.UsersService

class UserDetailsViewModel(
    private val usersService: UsersService
) : ViewModel() {
    private val _userDetails = MutableLiveData<UserDetails>()
    val userDetails: LiveData<UserDetails> = _userDetails

    fun loadUserDetails(userId: Long) {
        if (_userDetails.value != null) return
        try {
            _userDetails.value = usersService.getUserDetailsById(userId)
        } catch (e: UserNotFoundException) {
            e.printStackTrace()
        }
    }

    fun deleteUser() {
        val userDetails = this.userDetails.value ?: return
        usersService.deleteUser(userDetails.user)
    }
}