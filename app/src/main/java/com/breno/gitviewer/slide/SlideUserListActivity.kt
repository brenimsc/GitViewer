package com.breno.gitviewer.slide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.breno.gitviewer.R
import com.breno.gitviewer.databinding.ActivitySlideUserListBinding
import com.breno.gitviewer.follow.ui.FollowFragment
import com.breno.gitviewer.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SlideUserListActivity : AppCompatActivity() {

    private val binding: ActivitySlideUserListBinding by lazy {
        ActivitySlideUserListBinding.inflate(layoutInflater)
    }

    private val firstTab: String by lazy {
        intent?.getStringExtra(Constants.BUNDLE_FIRST_TAB).orEmpty()
    }

    private val user: String by lazy {
        intent?.getStringExtra(Constants.BUNDLE_USER).orEmpty()
    }

    private val pagerAdapter by lazy {
        ScreenSlidePagerAdapter(this, user)
    }

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.pager.adapter = pagerAdapter

        setupViews()
    }

    private fun setupViews() {
        tabLayout = binding.tabLayout
        viewPager = binding.pager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(pagerAdapter.tabs[position])
        }.attach()

        viewPager.currentItem = when (firstTab) {
            Constants.FOLLOWERS -> 1
            else -> 0
        }

    }

    inner class ScreenSlidePagerAdapter(fa: FragmentActivity, user: String) :
        FragmentStateAdapter(fa) {

        val tabs = arrayOf(R.string.following, R.string.followers)

        private val fragments = arrayOf(
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.BUNDLE_TYPE_REQUEST, Constants.FOLLOWING)
                    putString(Constants.BUNDLE_USER, user)
                }
            }, FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.BUNDLE_TYPE_REQUEST, Constants.FOLLOWERS)
                    putString(Constants.BUNDLE_USER, user)
                }
            })

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}