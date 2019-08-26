package com.android.githubproject.ui.github

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.android.githubproject.BR
import com.android.githubproject.R
import com.android.githubproject.ViewModelProviderFactory
import com.android.githubproject.databinding.ActivityGithubBinding
import com.android.githubproject.ui.base.BaseActivity
import com.android.githubproject.ui.main.MainNavigator
import com.android.githubproject.ui.main.MainViewModel
import com.android.githubproject.ui.main.UserInfoFragment
import com.android.githubproject.ui.main.repos.ReposFragment
import com.android.githubproject.ui.main.stargazers.StargazersFragment
import com.android.githubproject.util.rx.AppSchedulerProvider
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class GithubActivity : BaseActivity<ActivityGithubBinding, MainViewModel>(),
    MainNavigator,
    UserInfoFragment.OnFragmentInteractionListener,
    ReposFragment.OnFragmentInteractionListener,
    StargazersFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activityGithubViewBinding: ActivityGithubBinding
    private var userInfoFragment: UserInfoFragment =
        UserInfoFragment.newInstance()

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_github
    }

    override fun getViewModel(): MainViewModel {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        return mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.navigator = this
        activityGithubViewBinding = getViewDataBinding()
        activityGithubViewBinding.lifecycleOwner = this
        initUserInfoFragment()
        initTabLayout()
        mainViewModel.username = intent.getStringExtra("username")
        if (savedInstanceState == null)
            mainViewModel.fetchData()
    }

    private fun initUserInfoFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fl_activity_github, userInfoFragment).commit()
    }

    private fun initTabLayout() {
        activityGithubViewBinding.tlActivityGithub.addTab(
            activityGithubViewBinding.tlActivityGithub.newTab()
        )
        activityGithubViewBinding.tlActivityGithub.addTab(
            activityGithubViewBinding.tlActivityGithub.newTab()
        )
        activityGithubViewBinding.tlActivityGithub.tabGravity = TabLayout.GRAVITY_FILL
        activityGithubViewBinding.tlActivityGithub.setupWithViewPager(activityGithubViewBinding.vpActivityGithub)
        val viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            activityGithubViewBinding.tlActivityGithub.tabCount
        )
        activityGithubViewBinding.vpActivityGithub.adapter = viewPagerAdapter
    }

    override fun startNewActivity() {
        val intent = Intent(this, GithubActivity::class.java)
        if (mainViewModel.clickedItemOwnerName.value != null)
            intent.putExtra("username", mainViewModel.clickedItemOwnerName.value)
        else
            intent.putExtra("username", mainViewModel.clickedItemName.value)
        startActivity(intent)
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager, private val tabCount: Int) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val tabTitles = arrayOf("Repos", "Stargazers")


        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ReposFragment.newInstance()
                }
                1 -> {
                    StargazersFragment.newInstance()
                }
                else -> {
                    ReposFragment.newInstance()
                }
            }
        }

        override fun getCount(): Int {
            return tabCount
        }
    }
}
