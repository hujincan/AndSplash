package org.bubbble.andsplash.ui.search

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.ActivitySearchBinding
import org.bubbble.andsplash.ui.pictures.PictureFragment

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        // 展开状态使用状态栏和导航栏暗色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_VISIBLE
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_VISIBLE)
            }
        }
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.WHITE

        //API支持就更改Android NavigationBar Color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
        }else{
            window.navigationBarColor = Color.BLACK
        }

        binding = ActivitySearchBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        //1.设定RootView的TransitionName
        findViewById<View>(android.R.id.content).transitionName= resources.getString(R.string.shared_element_search_box)
        setEnterSharedElementCallback(
            MaterialContainerTransformSharedElementCallback()
        )
        // 2.设定进入的Transition
        window.sharedElementEnterTransition =
            MaterialContainerTransform().apply {
                duration = resources.getInteger(R.integer.motion_duration_small).toLong()
                addTarget(android.R.id.content)
            }
        //3.设定离开的Transition
        window.sharedElementReturnTransition =
            MaterialContainerTransform().apply {
                duration = resources.getInteger(R.integer.motion_duration_small).toLong()
                addTarget(android.R.id.content)
            }
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.run {
            viewPager.adapter = InfoAdapter(supportFragmentManager)
            tab.setupWithViewPager(binding.viewPager)
        }
    }

    companion object {

        private val INFO_TITLES = arrayOf(
            R.string.shot,
            R.string.nav_album,
            R.string.user
        )

        private val INFO_PAGES = arrayOf(
            { PictureFragment() },
            { PictureFragment() },
            { PictureFragment() }
        )
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
}