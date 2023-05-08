package com.example.recyclerview.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recyclerview.R
import com.example.recyclerview.databinding.FragmentUserDetailsBinding
import com.example.recyclerview.screens.viewmodels.UserDetailsViewModel
import com.example.recyclerview.screens.factory
import com.example.recyclerview.screens.navigator

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding: FragmentUserDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentUserDetailsBinding == null")

    private val viewModel: UserDetailsViewModel by lazy {
        ViewModelProvider(this, factory())[UserDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = getUserId()
        viewModel.loadUserDetails(userId)
        setListeners()
        observeViewModels()
    }

    private fun setListeners() {
        binding.deleteButton.setOnClickListener {
            viewModel.deleteUser()
            navigator().toast(R.string.user_has_been_deleted)
            navigator().goBack()
        }
    }

    private fun observeViewModels() {
        viewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            with(binding) {
                tvUserName.text = userDetails.user.name
                tvUserDetails.text = userDetails.details

                if (userDetails.user.photo.isNotBlank()) {
                    Glide.with(this@UserDetailsFragment)
                        .load(userDetails.user.photo)
                        .circleCrop()
                        .into(binding.ivUserPhoto)
                } else {
                    Glide.with(this@UserDetailsFragment)
                        .load(R.drawable.ic_user_avatar)
                        .into(binding.ivUserPhoto)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserId(): Long = requireArguments().getLong(USER_ID)

    companion object {
        private const val USER_ID = "USER_ID"
        fun newInstance(userId: Long): UserDetailsFragment {
            return UserDetailsFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, userId)
                }
            }
        }
    }
}