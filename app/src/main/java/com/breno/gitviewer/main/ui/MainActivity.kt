package com.breno.gitviewer.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.breno.gitviewer.adapter.AdapterPosts
import com.breno.gitviewer.databinding.ActivityMainBinding
import com.breno.gitviewer.main.viewmodel.MainViewModel
import com.breno.gitviewer.post.ui.PostsDetailActivity
import com.breno.gitviewer.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        AdapterPosts()
    }
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@MainActivity.viewModel
            recyclerPosts.adapter = adapter.apply {
                onClick = {
                    Intent(this@MainActivity, PostsDetailActivity::class.java).apply {
                        putExtra(Constants.BUNDLE_REPO, it.name)
                        putExtra(Constants.BUNDLE_USER, it.owner.login)
                    }.run {
                        startActivity(this)
                    }
                }
            }
        }

        viewModel.getPosts().observe(this) {
            it?.let {
                adapter.setList(it)
            }
        }

    }
}