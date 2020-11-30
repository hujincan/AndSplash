package org.bubbble.andsplash.ui.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.FragmentPersonalBinding
import org.bubbble.andsplash.databinding.ItemTabCountBinding
import org.bubbble.andsplash.shared.util.logger
import org.bubbble.andsplash.ui.pictures.PictureFragment
import kotlin.math.abs
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalFragment : Fragment() {

    private lateinit var binding: FragmentPersonalBinding
    private var tabItemList = ArrayList<View>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            viewPager.adapter = InfoAdapter(childFragmentManager)
            tab.setupWithViewPager(binding.viewPager)

            tab.removeAllTabs()
            tab.apply {
                addTab(newTab().setCustomView(addTabView(resources.getString(INFO_TITLES[0]), "0")))
                addTab(newTab().setCustomView(addTabView(resources.getString(INFO_TITLES[1]), "0")))
                addTab(newTab().setCustomView(addTabView(resources.getString(INFO_TITLES[2]), "0")))
            }

            appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                logger("appbar: ${(abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat())}")
                toolbarTitle.alpha = 1F - min(1F, (abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()) * 2)
                name.alpha = 1F - min(1F, (abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()) * 2)
                accountName.alpha = 1F - min(1F, (abs(verticalOffset) / appBarLayout.totalScrollRange.toFloat()) * 2)
            })
        }
    }

    private fun addTabView(title: String, count: String): View {
        val tabItem = ItemTabCountBinding.inflate(LayoutInflater.from(binding.tab.context), binding.tab, false)
        tabItem.title.text = title
        tabItem.count.text = count
        tabItemList.add(tabItem.root)
        return tabItem.root
    }

    companion object {

        private val INFO_TITLES = arrayOf(
            R.string.shot,
            R.string.favorite,
            R.string.nav_album
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