package org.bubbble.andsplash.ui

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivityMainBinding
import org.bubbble.andsplash.shared.data.ConnectionURL
import org.bubbble.andsplash.shared.result.EventObserver
import org.bubbble.andsplash.ui.personal.PersonalFragment
import org.bubbble.andsplash.ui.pictures.PictureFragment
import org.bubbble.andsplash.ui.search.SearchActivity
import org.bubbble.andsplash.ui.signin.SignInDialogFragment
import org.bubbble.andsplash.ui.signin.SignOutDialogFragment
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    companion object {
        private const val DIALOG_SIGN_IN = "dialog_sign_in"
        private const val DIALOG_SIGN_OUT = "dialog_sign_out"

        private const val NAV_ID_NONE = -1

        private val INFO_TITLES = arrayOf(
            R.string.nav_news,
            R.string.nav_album,
            R.string.nav_personal
        )

        private val INFO_PAGES = arrayOf(
            { PictureFragment() },
            { PictureFragment() },
            { PersonalFragment() }
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

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.TRANSPARENT
        }

        // 开启动画
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(
            MaterialContainerTransformSharedElementCallback()
        )
        window.sharedElementsUseOverlay = false

        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
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

        binding.searchBox.searchCard.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this, binding.searchBox.searchCard,
                resources.getString(R.string.shared_element_search_box)
            )
            startActivity(intent, options.toBundle())
        }

        binding.searchBox.authorIcon.setOnClickListener {
            viewModel.onProfileClicked()
        }

        viewModel.resultTest.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
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
                if (position == PERSONAL) {
                    toggleSearch(false)
                } else {
                    toggleSearch(true)
                }
            }
        })
    }

    private fun toggleSearch(isShow: Boolean){
        binding.searchBox.root.run {
            if (isShow) {
                ObjectAnimator.ofFloat(
                    this,
                    "translationY",
                    0f - (this.height + this.height + this.paddingBottom).toFloat(),
                    0f
                ).setDuration(200).start()
            } else {
                ObjectAnimator.ofFloat(
                    this,
                    "translationY",
                    0f,
                    0f - (this.height + this.height + this.paddingBottom).toFloat()
                ).setDuration(200).start()
            }
        }
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

    // 解决容器共享动画退出时动画问题
    override fun finishAfterTransition() {
        super.finish()
    }

    private fun openSignInDialog() {
        SignInDialogFragment().show(supportFragmentManager, DIALOG_SIGN_IN)
    }

    private fun openSignOutDialog() {
        SignOutDialogFragment().show(supportFragmentManager, DIALOG_SIGN_OUT)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null && intent.data != null && !TextUtils.isEmpty(intent.data!!.authority)
            && ConnectionURL.UNSPLASH_LOGIN_CALLBACK == intent.data!!.authority
        ) {
            val code = intent.data?.getQueryParameter("code")
            lifecycleScope.launch {
                viewModel.handleSignInResult(code)
            }
        }
    }
}