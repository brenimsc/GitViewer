package com.breno.gitviewer.user.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.breno.gitviewer.databinding.ActivityUserListBinding
import com.breno.gitviewer.extensions.backToActivityMain
import com.breno.gitviewer.extensions.goActivity
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.BACK
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.CLOSE
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.REQUEST_STARGAZERS
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.REQUEST_SUBSCRIBERS
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.REQUEST_USER
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.RESPONSE_ERROR
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.RESPONSE_GET_USER_ERROR
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.RESPONSE_GET_USER_SUCESS
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.RESPONSE_LIST_SUCESS
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.RESPONSE_LIST_USER_ERROR
import com.breno.gitviewer.user.viewmodel.UserListViewModel.Companion.USER_PERFIL
import com.breno.gitviewer.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserListActivity : AppCompatActivity() {

    private val type by lazy {
        intent?.getStringExtra(Constants.BUNDLE_TYPE_USER)
    }

    private val url by lazy {
        intent?.getStringExtra(Constants.BUNDLE_URL_USER)
    }

    private val viewModel: UserListViewModel by inject {
        parametersOf(type, url)
    }

    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@UserListActivity.viewModel
            lifecycleOwner = this@UserListActivity
        }

        viewModel.run {

            request.observe(this@UserListActivity) { REQUEST ->
                when (REQUEST) {
                    REQUEST_STARGAZERS -> executeRequestGetUsersStargazers()
                    REQUEST_SUBSCRIBERS -> executeRequestGetUsersSubscribers()
                    REQUEST_USER -> executeRequestGetUser(user?.login.orEmpty())
                }
            }

            response.observe(this@UserListActivity) { RESPONSE ->
                when (RESPONSE) {
                    RESPONSE_GET_USER_SUCESS -> onNavigation(USER_PERFIL)
                    RESPONSE_LIST_SUCESS -> setAdapter()
                    RESPONSE_ERROR,
                    RESPONSE_GET_USER_ERROR,
                    RESPONSE_LIST_USER_ERROR -> setErrorEnabled()
                }
            }

            navigation.observe(this@UserListActivity) { NAVIGATION ->
                when (NAVIGATION) {
                    USER_PERFIL -> goActivity(
                        UserDetailsActivity::class.java,
                        bundleOf(Constants.BUNDLE_USER to viewModel.user)
                    )
                    BACK -> onBackPressed()
                    CLOSE -> backToActivityMain()
                }
            }

        }
    }
}