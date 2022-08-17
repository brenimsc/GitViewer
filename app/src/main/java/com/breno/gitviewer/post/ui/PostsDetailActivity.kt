package com.breno.gitviewer.post.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.breno.gitviewer.R
import com.breno.gitviewer.databinding.ActivityPostsDetailBinding
import com.breno.gitviewer.goActivity
import com.breno.gitviewer.post.viewmodel.PostDetailViewModel
import com.breno.gitviewer.adapter.AdapterEvents
import com.breno.gitviewer.user.ui.UserDetailsActivity
import com.breno.gitviewer.user.ui.UserListActivity
import com.breno.gitviewer.user.viewmodel.UserListViewModel
import com.breno.gitviewer.utils.Constants
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PostsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostsDetailBinding

    private val adapterEvent by lazy {
        AdapterEvents(this)
    }

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

        binding.viewModel = viewModel

        binding.recyclerEventsDetailsPosts.adapter = adapterEvent

        viewModel.post.observe(this) { repositoryGit ->
            binding.apply {
                txtAttDetailsPosts.text = repositoryGit.updated_at
                txtContribuitorsQtdDetailsPosts.text =
                    getString(R.string.contribuitors, repositoryGit.subscribers_count.toString())
                txtDescriptionDetailsPost.text = repositoryGit.description
                txtNameUserDetailsPosts.text = repositoryGit.owner?.login
                txtStarsQtdDetailsPosts.text =
                    getString(R.string.stars, repositoryGit.stargazers_count.toString())
                txtSubscribesQtdDetailsPosts.text =
                    getString(R.string.subscribes, repositoryGit.subscribers_count.toString())
                txtVisualizadoresQtdDetailsPosts.text =
                    getString(R.string.visualizacao, repositoryGit.watchers_count.toString())
                Glide.with(this@PostsDetailActivity).load(repositoryGit.owner?.avatar_url)
                    .centerCrop()
                    .into(imgUserDetailsPosts)
            }
        }

        viewModel.events.observe(this) { events ->
            adapterEvent.setList(events)
        }

        viewModel.navigation.observe(this) {
            when (it) {
                PostDetailViewModel.USER_PERFIL -> viewModel.onRequest(PostDetailViewModel.REQUEST_USER)

                PostDetailViewModel.USER_LIST_CONTRIBUITORS -> Intent(
                    this,
                    UserListActivity::class.java
                ).apply {
                    putExtra(Constants.BUNDLE_TYPE_USER, UserListViewModel.CONTRIBUTORS)
                    putExtra(
                        Constants.BUNDLE_URL_USER,
                        viewModel.post.value?.contributors_url
                    )
                    startActivity(this)
                }
                PostDetailViewModel.USER_LIST_SUBSCRIBERS -> Intent(
                    this,
                    UserListActivity::class.java
                ).apply {
                    putExtra(Constants.BUNDLE_TYPE_USER, UserListViewModel.SUBSCRIBERS)
                    putExtra(
                        Constants.BUNDLE_URL_USER,
                        viewModel.post.value?.contributors_url
                    )
                    startActivity(this)
                }
                PostDetailViewModel.USER_LIST_STARGAZERS -> Intent(
                    this,
                    UserListActivity::class.java
                ).apply {
                    putExtra(Constants.BUNDLE_TYPE_USER, UserListViewModel.STARGAZERS)
                    putExtra(
                        Constants.BUNDLE_URL_USER,
                        viewModel.post.value?.contributors_url
                    )
                    startActivity(this)
                }
            }
        }

        viewModel.request.observe(this) {
            when (it) {
                PostDetailViewModel.REQUEST_USER -> viewModel.executeRequestUser()
            }
        }

        viewModel.response.observe(this) {
            when (it) {
                PostDetailViewModel.REQUEST_USER_OK -> goActivity(
                    UserDetailsActivity::class.java,
                    Constants.BUNDLE_USER,
                    viewModel.user
                )
            }
        }
    }
}