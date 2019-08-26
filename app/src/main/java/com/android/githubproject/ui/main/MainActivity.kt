package com.android.githubproject.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.android.githubproject.BR
import com.android.githubproject.R
import com.android.githubproject.ViewModelProviderFactory
import com.android.githubproject.databinding.ActivityMainBinding
import com.android.githubproject.ui.base.BaseActivity
import com.android.githubproject.ui.github.GithubActivity
import com.android.githubproject.ui.main.repos.ReposFragment
import com.android.githubproject.ui.main.stargazers.StargazersFragment
import com.android.githubproject.util.rx.AppSchedulerProvider
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    MainNavigator,
    UserInfoFragment.OnFragmentInteractionListener,
    ReposFragment.OnFragmentInteractionListener,
    StargazersFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activityMainViewBinding: ActivityMainBinding
    private var userInfoFragment: UserInfoFragment =
        UserInfoFragment.newInstance()

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        return mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.navigator = this
        activityMainViewBinding = getViewDataBinding()
        activityMainViewBinding.lifecycleOwner = this
        initUserInfoFragment()
        initTabLayout()
        mainViewModel.username = "googlesamples"
        if (savedInstanceState == null)
            mainViewModel.fetchData()
    }

    private fun initUserInfoFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fl_activity_main, userInfoFragment).commit()
    }

    private fun initTabLayout() {
        activityMainViewBinding.tlActivityMain.addTab(
            activityMainViewBinding.tlActivityMain.newTab()
        )
        activityMainViewBinding.tlActivityMain.addTab(
            activityMainViewBinding.tlActivityMain.newTab()
        )
        activityMainViewBinding.tlActivityMain.tabGravity = TabLayout.GRAVITY_FILL
        activityMainViewBinding.tlActivityMain.setupWithViewPager(activityMainViewBinding.vpActivityMain)
        val viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            activityMainViewBinding.tlActivityMain.tabCount
        )
        activityMainViewBinding.vpActivityMain.adapter = viewPagerAdapter
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
