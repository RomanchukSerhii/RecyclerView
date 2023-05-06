package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserListener
import com.example.recyclerview.model.UsersService
import com.example.recyclerview.model.adapter.UserActionListener
import com.example.recyclerview.model.adapter.UserListAdapter

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val userListAdapter: UserListAdapter by lazy {
        UserListAdapter(object : UserActionListener {
            override fun onUserMove(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                usersService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(
                    this@MainActivity,
                    "${user.name} - ${user.company}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        usersService.addListener(usersListener)
        binding.recyclerView.adapter = userListAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }

    private val usersListener: UserListener = {
        userListAdapter.submitList(it)
    }
}