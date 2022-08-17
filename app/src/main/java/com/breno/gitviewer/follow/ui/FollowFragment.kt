package com.breno.gitviewer.follow.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.breno.gitviewer.slide.SlideUserListActivity
import com.breno.gitviewer.databinding.FollowBinding
import com.breno.gitviewer.follow.viewmodel.FollowViewModel
import com.breno.gitviewer.adapter.AdapterUserList
import com.breno.gitviewer.goActivity
import com.breno.gitviewer.user.ui.UserDetailsActivity
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import com.breno.gitviewer.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class FollowFragment : Fragment() {


    private val adapter by lazy {
        AdapterUserList().apply {
            onClick = {
                viewModel.executeRequestGetUser(it.login)
            }
        }
    }

    private val request: String? by lazy {
        arguments?.getString(Constants.BUNDLE_TYPE_REQUEST)
    }

    private val user: String by lazy {
        arguments?.getString(Constants.BUNDLE_USER).orEmpty()
    }

    private val viewModel: FollowViewModel by inject {
        parametersOf(request)
    }

    private lateinit var binding: FollowBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FollowBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerFollow.adapter = adapter

        viewModel.typeRequest.observe(viewLifecycleOwner) {
            when (it) {
                Constants.FOLLOWERS -> {
                    viewModel.getUserFollowers(user)
                }
                Constants.FOLLOWING -> {
                    viewModel.getUserFollowing(user)
                }
            }
        }

        viewModel.users.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }

        viewModel.response.observe(viewLifecycleOwner) {
            when (it) {
                UserListViewModel.RESPONSE_SUCESS -> {
                    Intent(
                        requireContext(),
                        UserDetailsActivity::class.java
                    ).apply {
                        putExtra(
                            Constants.BUNDLE_USER,
                            viewModel.user
                        )
                        startActivity(this)
                    }
                }
            }
        }
    }

}