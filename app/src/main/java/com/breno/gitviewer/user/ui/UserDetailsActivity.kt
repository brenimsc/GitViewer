package com.breno.gitviewer.user.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.breno.gitviewer.databinding.PerfilUserBinding
import com.breno.gitviewer.extensions.backToActivityMain
import com.breno.gitviewer.extensions.goActivity
import com.breno.gitviewer.extensions.goToLink
import com.breno.gitviewer.model.User
import com.breno.gitviewer.slide.ui.SlideUserListActivity
import com.breno.gitviewer.user.SearchUserActivity
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.BACK
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.CLOSE
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.REQUEST_GET_USER_ERROR
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.REQUEST_GET_USER_SUCESS
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.SEARCH_USER
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.USERS_FOLLOWERS
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.USERS_FOLLOWING
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel.Companion.VIEW_GITHUB
import com.breno.gitviewer.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: PerfilUserBinding

    private val user by lazy {
        intent?.getParcelableExtra(Constants.BUNDLE_USER) as? User
    }

    private val viewModel: UserDetailsViewModel by inject {
        parametersOf(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PerfilUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@UserDetailsActivity.viewModel
            lifecycleOwner = this@UserDetailsActivity
        }

        viewModel.run {
            response.observe(this@UserDetailsActivity) { RESPONSE ->
                when (RESPONSE) {
                    REQUEST_GET_USER_SUCESS -> setAdapter()
                    REQUEST_GET_USER_ERROR -> setErrorEnabled()
                }
            }

            navigation.observe(this@UserDetailsActivity) { NAVIGATION ->
                when (NAVIGATION) {
                    USERS_FOLLOWERS -> goActivity(
                        SlideUserListActivity::class.java,
                        bundleOf(
                            Constants.BUNDLE_USER to user.login,
                            Constants.BUNDLE_FIRST_TAB to Constants.FOLLOWERS
                        )
                    )
                    USERS_FOLLOWING -> goActivity(
                        SlideUserListActivity::class.java,
                        bundleOf(
                            Constants.BUNDLE_USER to user.login,
                            Constants.BUNDLE_FIRST_TAB to Constants.FOLLOWING
                        )
                    )
                    SEARCH_USER -> goActivity(
                        SearchUserActivity::class.java,
                        bundleOf()
                    )
                    VIEW_GITHUB -> goToLink(user.html_url)
                    BACK -> onBackPressed()
                    CLOSE -> backToActivityMain()
                }
            }
        }
    }
}