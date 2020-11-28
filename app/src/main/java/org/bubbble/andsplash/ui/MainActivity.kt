package org.bubbble.andsplash.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityMainBinding
import org.bubbble.andsplash.shared.result.EventObserver
import org.bubbble.andsplash.ui.settings.SettingsFragment
import org.bubbble.andsplash.ui.signin.SignInDialogFragment
import org.bubbble.andsplash.ui.signin.SignOutDialogFragment
import org.bubbble.andsplash.util.navigationItemBackground


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    companion object {
        private const val DIALOG_SIGN_IN = "dialog_sign_in"
        private const val DIALOG_SIGN_OUT = "dialog_sign_out"

        private const val NAV_ID_NONE = -1

        private val INFO_TITLES = arrayOf(
            R.id.nav_news,
            R.id.nav_album,
            R.id.nav_personal
        )

        private val INFO_PAGES = arrayOf(
            { SettingsFragment() },
            { SettingsFragment() },
            { SettingsFragment() }
        )

        const val NEWS = 0
        const val ALBUM = 1
        const val PERSONAL = 2
    }
    private var currentNavId = NAV_ID_NONE
    private lateinit var navController: NavController

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.navigateToSignInDialogAction.observe(this, EventObserver {
            openSignInDialog()
        })

        viewModel.navigateToSignOutDialogAction.observe(this, EventObserver {
            openSignOutDialog()
        })

        lifecycleScope.launch {
            viewModel.handleSignInResult(null)
        }

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        binding.viewPager.offscreenPageLimit = INFO_PAGES.size
        binding.viewPager.adapter = InfoAdapter(supportFragmentManager)
        binding.navigation.setOnNavigationItemSelectedListener(this)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                binding.navigation.menu.getItem(position).isChecked = true
            }
        })
    }

    /**
     * Adapter that builds a page for each info screen.
     */
    inner class InfoAdapter(
        fm: FragmentManager
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount() = INFO_PAGES.size

        override fun getItem(position: Int) = INFO_PAGES[position]()

        override fun getPageTitle(position: Int): CharSequence {
            return resources.getString(INFO_TITLES[position])
        }
    }

    override fun onBackPressed() {

        if (binding.viewPager.currentItem != NEWS) {
            binding.viewPager.currentItem = NEWS
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_news -> binding.viewPager.currentItem = NEWS
            R.id.nav_album -> binding.viewPager.currentItem = ALBUM
            R.id.nav_personal -> binding.viewPager.currentItem = PERSONAL
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SignInDialogFragment.REQUEST_CODE_SIGN_IN) {
            lifecycleScope.launch {
                viewModel.handleSignInResult(data)
            }
        }
    }

    private fun openSignInDialog() {
        SignInDialogFragment().show(supportFragmentManager, DIALOG_SIGN_IN)
    }

    private fun openSignOutDialog() {
        SignOutDialogFragment().show(supportFragmentManager, DIALOG_SIGN_OUT)
    }
}