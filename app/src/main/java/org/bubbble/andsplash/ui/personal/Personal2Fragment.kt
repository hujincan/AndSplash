package org.bubbble.andsplash.ui.personal

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import com.google.android.material.appbar.AppBarLayout
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.FragmentPersonal2Binding
import org.bubbble.andsplash.databinding.FragmentPersonalBinding
import org.bubbble.andsplash.databinding.ItemTabCountBinding
import org.bubbble.andsplash.shared.util.logger
import org.bubbble.andsplash.ui.MainActivityViewModel
import org.bubbble.andsplash.ui.editor.EditUserActivity
import org.bubbble.andsplash.ui.pictures.Page
import org.bubbble.andsplash.ui.pictures.PictureFragment
import kotlin.math.min


class Personal2Fragment : Fragment() {

    private lateinit var binding: FragmentPersonal2Binding
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private var tabItemList = ArrayList<View>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonal2Binding.inflate(inflater, container, false)
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

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activityViewModel.currentUserInfo.observe(viewLifecycleOwner, { userEntity ->

            for ((index, tabItem) in tabItemList.withIndex()) {
                when (index) {
                    0 -> tabItem.findViewById<TextView>(R.id.count).text = userEntity.total_photos?.toString()
                    1 -> tabItem.findViewById<TextView>(R.id.count).text = userEntity.total_likes?.toString()
                    2 -> tabItem.findViewById<TextView>(R.id.count).text = userEntity.total_collections?.toString()
                }
            }
        })
    }

    private fun addTabView(title: String, count: String): View {
        val tabItem = ItemTabCountBinding.inflate(
            LayoutInflater.from(binding.tab.context),
            binding.tab,
            false
        )
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
            { PictureFragment.newInstance(Page.PERSONAL_PHOTO) },
            { PictureFragment.newInstance(Page.PERSONAL_LIKE) },
            { PictureFragment.newInstance(Page.PERSONAL_COLLECTION) }
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