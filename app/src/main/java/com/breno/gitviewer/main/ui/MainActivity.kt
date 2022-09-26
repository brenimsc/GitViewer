package com.breno.gitviewer.main.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.breno.gitviewer.databinding.ActivityMainBinding
import com.breno.gitviewer.extensions.goActivity
import com.breno.gitviewer.main.viewmodel.MainViewModel
import com.breno.gitviewer.main.viewmodel.MainViewModel.Companion.POST_SELECTED
import com.breno.gitviewer.main.viewmodel.MainViewModel.Companion.REQUEST_POST_ERROR
import com.breno.gitviewer.main.viewmodel.MainViewModel.Companion.REQUEST_POST_SUCCESS
import com.breno.gitviewer.post.ui.PostsDetailActivity
import com.breno.gitviewer.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }

        viewModel.run {

            response.observe(this@MainActivity) { REQUEST ->
                when (REQUEST) {
                    REQUEST_POST_SUCCESS -> setAdapter()
                    REQUEST_POST_ERROR -> setErrorEnabled()
                }
            }

            error.observe(this@MainActivity) {
                Log.e("BRENOL", "Erro: $it")
            }

            navigation.observe(this@MainActivity) { NAVIGATION ->
                when (NAVIGATION) {
                    POST_SELECTED -> goActivity(
                        PostsDetailActivity::class.java,
                        bundleOf(
                            Constants.BUNDLE_REPO to postSelected.name,
                            Constants.BUNDLE_USER to postSelected.owner.login
                        )
                    )
                }
            }

        }

    }

}