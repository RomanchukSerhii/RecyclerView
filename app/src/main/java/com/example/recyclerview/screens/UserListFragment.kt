package com.example.recyclerview.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.recyclerview.App
import com.example.recyclerview.R
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.databinding.FragmentUserListBinding
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserListener
import com.example.recyclerview.model.UsersService
import com.example.recyclerview.model.adapter.UserActionListener
import com.example.recyclerview.model.adapter.UserListAdapter

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding: FragmentUserListBinding
        get() = _binding ?: throw RuntimeException("FragmentUserListBinding == null")

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
                    requireContext(),
                    "${user.name} - ${user.company}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onUserFire(user: User) {
                usersService.fireUser(user)
            }
        })
    }

    private val usersService: UsersService
        get() = (requireContext().applicationContext as App).usersService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersService.addListener(usersListener)
        binding.recyclerView.adapter = userListAdapter
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        usersService.removeListener(usersListener)
        _binding = null
    }

    private val usersListener: UserListener = {
        userListAdapter.submitList(it)
    }
}