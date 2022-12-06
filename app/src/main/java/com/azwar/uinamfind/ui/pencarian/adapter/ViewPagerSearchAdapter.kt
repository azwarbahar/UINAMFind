package com.azwar.uinamfind.ui.pencarian.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azwar.uinamfind.ui.pencarian.fragment.*

class ViewPagerSearchAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, num_tab: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    val num_tabs = num_tab
    override fun getItemCount(): Int {
        return num_tabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MahasiswaSearchFragment()
            1 -> return LokerSearchFragment()
            2 -> return BeasiswaSearchFragment()
            3 -> return MagangSearchFragment()
            4 -> return LembagaSearchFragment()
            5 -> return UkmSearchFragment()
        }

        return OrganisasiSearchFragment()
    }
}