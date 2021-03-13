package org.bubbble.andsplash.ui.popular

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.bubbble.andsplash.R
import org.bubbble.andsplash.databinding.FragmentPopularBinding
import org.bubbble.andsplash.ui.MainActivityViewModel
import org.bubbble.andsplash.ui.pictures.Page
import org.bubbble.andsplash.ui.pictures.PictureFragment
import org.bubbble.andsplash.ui.search.SearchActivity
import org.bubbble.andsplash.util.load

class PopularFragment : Fragment() {

    private lateinit var binding: FragmentPopularBinding
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    companion object {
        private val INFO_TITLES = arrayOf(
            R.string.nav_news,
            R.string.nav_album
        )

        private val INFO_PAGES = arrayOf(
            PictureFragment.newInstance(Page.HOME_PHOTO),
            PictureFragment.newInstance(Page.HOME_COLLECTION)
        )

        const val NEWS = 0
        const val ALBUM = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.offscreenPageLimit = INFO_PAGES.size
        binding.viewPager.adapter = InfoAdapter(childFragmentManager)
//        binding.tab.setupWithViewPager(binding.viewPager)

//        binding.searchBox.searchCard.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            val options = ActivityOptions.makeSceneTransitionAnimation(
//                activity, binding.searchBox.searchCard,
//                resources.getString(R.string.shared_element_search_box)
//            )
//            startActivity(intent, options.toBundle())
//        }
//
//        binding.searchBox.authorIcon.setOnClickListener {
//            activityViewModel.onProfileClicked()
//        }
//
//        binding.searchBox.viewType.setOnClickListener {
//            INFO_PAGES[binding.viewPager.currentItem].changeViewType()
//        }
//
//        activityViewModel.currentUserInfo.observe(viewLifecycleOwner, Observer { userEntity ->
//            userEntity.profile_image?.let {
//                    binding.searchBox.authorIcon.load(it)
//                } ?: run {
//                    binding.searchBox.authorIcon.load(R.drawable.ic_default_profile_avatar)
//                }
//
//            }
//        )

    }

    /**
     * Adapter that builds a page for each info screen.
     */
    inner class InfoAdapter(
        fm: FragmentManager
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount() = INFO_PAGES.size

        override fun getItem(position: Int) = INFO_PAGES[position]

        override fun getPageTitle(position: Int): CharSequence {
            return resources.getString(INFO_TITLES[position])
        }
    }

}