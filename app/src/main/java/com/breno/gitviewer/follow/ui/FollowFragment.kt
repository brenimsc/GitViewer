package com.breno.gitviewer.follow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.breno.gitviewer.databinding.FollowBinding
import com.breno.gitviewer.extensions.goActivity
import com.breno.gitviewer.follow.viewmodel.FollowViewModel
import com.breno.gitviewer.user.ui.UserDetailsActivity
import com.breno.gitviewer.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class FollowFragment : Fragment() {

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
    ): View {
        binding = FollowBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.run {
            request.observe(viewLifecycleOwner) { REQUEST ->
                when (REQUEST) {
                    Constants.FOLLOWERS -> {
                        viewModel.getUserFollowers(this@FollowFragment.user)
                    }
                    Constants.FOLLOWING -> {
                        viewModel.getUserFollowing(this@FollowFragment.user)
                    }
                }
            }

            response.observe(viewLifecycleOwner) { RESPONSE ->
                when (RESPONSE) {
                    FollowViewModel.RESPONSE_GET_USER_SUCESS -> onNavigation(FollowViewModel.USER_PERFIL)
                    FollowViewModel.RESPONSE_GET_USER_FOLLOWERS_SUCESS,
                    FollowViewModel.RESPONSE_GET_USER_FOLLOWING_SUCESS -> setAdapter()
                }
            }

            navigation.observe(viewLifecycleOwner) { NAVIGATION ->
                when (NAVIGATION) {
                    FollowViewModel.USER_PERFIL -> goActivity(
                        UserDetailsActivity::class.java,
                        bundleOf(
                            Constants.BUNDLE_USER to
                                    viewModel.user
                        )
                    )
                }
            }
        }
    }
}