package com.azwar.uinamfind.ui.lembaga.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azwar.uinamfind.ui.lembaga.fragment.AnggotaLembagaFragment
import com.azwar.uinamfind.ui.lembaga.fragment.FotoLembagaFragment
import com.azwar.uinamfind.ui.lembaga.fragment.PengaturanLembagaFragment
import com.azwar.uinamfind.ui.lembaga.fragment.TentangLembagaFragment

//private const val NUM_TABS = 3

class ViewPagerLembagaAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, num_tab: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    val num_tabs = num_tab
    override fun getItemCount(): Int {
        return num_tabs
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TentangLembagaFragment()
            1 -> return AnggotaLembagaFragment()
            2 -> return FotoLembagaFragment()
        }
        return PengaturanLembagaFragment()
    }
}