package com.breno.gitviewer.post.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.breno.gitviewer.databinding.ActivityPostsDetailBinding
import com.breno.gitviewer.extensions.goActivity
import com.breno.gitviewer.extensions.goActivityWithCondition
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.BACK
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_ERROR
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_EVENTS
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_EVENTS_ERROR
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_EVENTS_SUCCESS
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_REPO_ERROR
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_REPO_SUCCESS
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_USER
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_USER_ERROR
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.REQUEST_USER_SUCCESS
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.USER_LIST_STARGAZERS
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.USER_LIST_SUBSCRIBERS
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel.Companion.USER_LOADED
import com.breno.gitviewer.user.ui.UserDetailsActivity
import com.breno.gitviewer.user.ui.UserListActivity
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import com.breno.gitviewer.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PostsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostsDetailBinding

    private val viewModel: PostDetailViewModel by viewModel {
        parametersOf(
            intent.getStringExtra(Constants.BUNDLE_REPO),
            intent.getStringExtra(Constants.BUNDLE_USER)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@PostsDetailActivity.viewModel
            lifecycleOwner = this@PostsDetailActivity
        }

        viewModel.run {

            navigation.observe(this@PostsDetailActivity) { NAVIGATION ->
                when (NAVIGATION) {
                    USER_LIST_SUBSCRIBERS -> goActivityWithCondition(
                        UserListActivity::class.java,
                        bundleOf(
                            Constants.BUNDLE_TYPE_USER to UserListViewModel.SUBSCRIBERS,
                            Constants.BUNDLE_URL_USER to repositoryGit.value?.subscribers_url
                        ),
                        !repositoryGit.value?.contributors_url.isNullOrBlank()
                    )

                    USER_LIST_STARGAZERS -> goActivityWithCondition(
                        UserListActivity::class.java,
                        bundleOf(
                            Constants.BUNDLE_TYPE_USER to UserListViewModel.STARGAZERS,
                            Constants.BUNDLE_URL_USER to repositoryGit.value?.stargazers_url
                        ),
                        !repositoryGit.value?.contributors_url.isNullOrBlank()
                    )

                    USER_LOADED -> goActivity(
                        UserDetailsActivity::class.java,
                        bundleOf(Constants.BUNDLE_USER to user)
                    )

                    BACK -> onBackPressed()
                }
            }

            request.observe(this@PostsDetailActivity) { REQUEST ->
                when (REQUEST) {
                    REQUEST_USER -> executeRequestUser()
                    REQUEST_EVENTS -> executeRequestEvents(repo, nameUser)
                }
            }

            response.observe(this@PostsDetailActivity) { RESPONSE ->
                when (RESPONSE) {
                    REQUEST_USER_SUCCESS -> onNavigation(USER_LOADED)
                    REQUEST_EVENTS_SUCCESS -> setAdapter()
                    REQUEST_REPO_SUCCESS -> onRequest(REQUEST_EVENTS)
                    REQUEST_ERROR, REQUEST_USER_ERROR, REQUEST_EVENTS_ERROR, REQUEST_REPO_ERROR -> setErrorEnabled()
                }
            }

        }
    }
}