package com.breno.gitviewer.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.breno.gitviewer.databinding.ActivitySearchUserBinding
import com.breno.gitviewer.extensions.backToActivityMain
import com.breno.gitviewer.extensions.goActivity
import com.breno.gitviewer.user.SearchUserViewModel.Companion.BACK
import com.breno.gitviewer.user.SearchUserViewModel.Companion.CLOSE
import com.breno.gitviewer.user.SearchUserViewModel.Companion.REQUEST_GET_USER_ERROR
import com.breno.gitviewer.user.SearchUserViewModel.Companion.REQUEST_GET_USER_SUCESS
import com.breno.gitviewer.user.SearchUserViewModel.Companion.USER_PERFIL
import com.breno.gitviewer.user.ui.UserDetailsActivity
import com.breno.gitviewer.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchUserActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySearchUserBinding

    private val viewModel by viewModel<SearchUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            lifecycleOwner = this@SearchUserActivity
            viewModel = this@SearchUserActivity.viewModel
        }

        viewModel.run {
            response.observe(this@SearchUserActivity) { RESPONSE ->
                when(RESPONSE) {
                    REQUEST_GET_USER_SUCESS -> setAdapter()
                    REQUEST_GET_USER_ERROR -> setErrorEnabled()
                }
            }

            navigation.observe(this@SearchUserActivity) { NAVIGATION ->
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