package org.bubbble.andsplash.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author Andrew
 * @date 2020/08/04 18:11
 * ViewPager 装载Fragment的适配器
 */
class ViewPagerFragmentAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    /**
     * 存储Fragment
     */
    private val fragmentList = ArrayList<Fragment>()

    /**
     * 存储导航用到的Title
     */
    private val fragmentTitleList = ArrayList<String>()

    /**
     * 获取某个Fragment
     */
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    /**
     * 获取Fragment总数量
     */
    override fun getCount(): Int {
        return fragmentList.size
    }

    /**
     * 添加Fragment
     */
    fun addFragment(fragment: Fragment, title: String): ViewPagerFragmentAdapter {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
        return this
    }

    /**
     * 获取Fragment的Title标识
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}