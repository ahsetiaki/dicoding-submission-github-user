package com.setiaki.githubusersubmission2

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserListFragment.newInstance(UserListFragment.TYPE_FOLLOWERS)
            1 -> fragment = UserListFragment.newInstance(UserListFragment.TYPE_FOLLOWING)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2

}