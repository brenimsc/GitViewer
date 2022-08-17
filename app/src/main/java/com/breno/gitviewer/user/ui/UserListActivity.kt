package com.breno.gitviewer.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.breno.gitviewer.databinding.ActivityUserListBinding
import com.breno.gitviewer.goActivity
import com.breno.gitviewer.adapter.AdapterUserList
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import com.breno.gitviewer.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserListActivity : AppCompatActivity() {

    private val binding: ActivityUserListBinding by lazy {
        ActivityUserListBinding.inflate(layoutInflater)
    }

    private val type by lazy {
        intent?.getStringExtra(Constants.BUNDLE_TYPE_USER)
    }

    private val url by lazy {
        intent?.getStringExtra(Constants.BUNDLE_URL_USER)
    }

    private val viewModel: UserListViewModel by inject {
        parametersOf(type, url)
    }

    private val adapter: AdapterUserList by lazy {
        AdapterUserList().apply {
            onClick = {
                viewModel.executeRequestGetUser(it.login)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.apply {
            viewModel = this@UserListActivity.viewModel
            recyclerUserList.adapter = adapter
        }.root)

        viewModel.request.observe(this) {
            Log.e("BRENOL", it.toString())
            when (it) {
                UserListViewModel.REQUEST_STARGAZERS -> viewModel.executeRequestGetUsersStargazers()
                UserListViewModel.REQUEST_CONTRIBUTORS -> viewModel.executeRequestGetUsersContributors()
                UserListViewModel.REQUEST_SUBSCRIBERS -> viewModel.executeRequestGetUsersSubscribers()
            }
        }

        viewModel.response.observe(this) {
            when (it) {
                UserListViewModel.RESPONSE_SUCESS -> goActivity(
                    UserDetailsActivity::class.java,
                    Constants.BUNDLE_USER,
                    viewModel.user
                )
            }
        }

        viewModel.listUser.observe(this) {
            adapter.setList(it)
        }
    }
}