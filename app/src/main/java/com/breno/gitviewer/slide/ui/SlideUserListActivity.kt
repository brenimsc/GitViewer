package com.breno.gitviewer.slide.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.breno.gitviewer.R
import com.breno.gitviewer.databinding.ActivitySlideUserListBinding
import com.breno.gitviewer.follow.ui.FollowFragment
import com.breno.gitviewer.slide.viewmodel.SlideUserListViewModel
import com.breno.gitviewer.slide.viewmodel.SlideUserListViewModel.Companion.BACK
import com.breno.gitviewer.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SlideUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlideUserListBinding

    private val firstTab: String by lazy {
        intent?.getStringExtra(Constants.BUNDLE_FIRST_TAB).orEmpty()
    }

    private val user: String by lazy {
        intent?.getStringExtra(Constants.BUNDLE_USER).orEmpty()
    }

    private val pagerAdapter by lazy {
        ScreenSlidePagerAdapter(this, user)
    }

    private val viewModel: SlideUserListViewModel by inject {
        parametersOf(firstTab)
    }

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlideUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel = this@SlideUserListActivity.viewModel
            lifecycleOwner = this@SlideUserListActivity
            pager.adapter = pagerAdapter
            this@SlideUserListActivity.tabLayout = tabLayout
            viewPager = binding.pager
        }

        setupTabLayoutTitle()

        viewModel.run {
            navigation.observe(this@SlideUserListActivity) { NAVIGATION ->
                when (NAVIGATION) {
                    BACK -> onBackPressed()
                }
            }
        }
    }

    private fun setupTabLayoutTitle() {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(pagerAdapter.tabs[position])
        }.attach()
    }

    inner class ScreenSlidePagerAdapter(fa: FragmentActivity, user: String) :
        FragmentStateAdapter(fa) {

        val tabs = arrayOf(R.string.followers, R.string.following)

        private val fragments = arrayOf(
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.BUNDLE_TYPE_REQUEST, Constants.FOLLOWERS)
                    putString(Constants.BUNDLE_USER, user)
                }
            }, FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.BUNDLE_TYPE_REQUEST, Constants.FOLLOWING)
                    putString(Constants.BUNDLE_USER, user)
                }
            })

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int): FollowFragment = fragments[position]
    }
}