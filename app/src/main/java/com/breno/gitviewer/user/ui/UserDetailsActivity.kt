package com.breno.gitviewer.user.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.breno.gitviewer.follow.ui.FollowFragment
import com.breno.gitviewer.slide.SlideUserListActivity
import com.breno.gitviewer.databinding.ItemUserBinding
import com.breno.gitviewer.model.User
import com.breno.gitviewer.adapter.AdapterRepository
import com.breno.gitviewer.user.viewmodel.UserDetailsViewModel
import com.breno.gitviewer.utils.Constants
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ItemUserBinding

    private val user by lazy {
        intent?.getSerializableExtra(Constants.BUNDLE_USER) as User
    }

    private val adapter: AdapterRepository by lazy {
        AdapterRepository(this@UserDetailsActivity)
    }

    private val viewModel: UserDetailsViewModel by inject {
        parametersOf(user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@UserDetailsActivity.viewModel
            Glide.with(binding.root)
                .load(user.avatar_url)
                .into(imgItemUser)
            recyclerPostItemUser.adapter = adapter
        }

        viewModel.repositories.observe(this) { list ->
            list?.takeIf {
                true
            }?.run {
                adapter.setList(this)
            }
        }

        viewModel.navigation.observe(this) {
            when (it) {
                UserDetailsViewModel.USERS_FOLLOWERS -> {
                    Intent(this, SlideUserListActivity::class.java).apply {
                        putExtra(Constants.BUNDLE_USER, user.login)
                        putExtra(Constants.BUNDLE_FIRST_TAB, Constants.FOLLOWERS)
                        startActivity(this)
                    }
                }
                UserDetailsViewModel.USERS_FOLLOWING -> {
                    Intent(this, SlideUserListActivity::class.java).apply {
                        putExtra(Constants.BUNDLE_USER, user.login)
                        putExtra(Constants.BUNDLE_FIRST_TAB, Constants.FOLLOWING)
                        startActivity(this)
                    }
                }
            }
        }
    }
}