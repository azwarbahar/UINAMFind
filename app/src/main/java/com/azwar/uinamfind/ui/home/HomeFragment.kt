package com.azwar.uinamfind.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.FragmentHomeBinding
import com.azwar.uinamfind.data.models.BeasiswaModel
import com.azwar.uinamfind.data.models.LokerModel
import com.azwar.uinamfind.ui.beasiswa.BeasiswaActivity
import com.azwar.uinamfind.ui.home.adapter.BeasiswaTerbaruAdapter
import com.azwar.uinamfind.ui.home.adapter.CardMahasiswaAdapter
import com.azwar.uinamfind.ui.home.adapter.LokerHomeAdapter
import com.azwar.uinamfind.ui.lembaga.LembagaActivity
import com.azwar.uinamfind.ui.loker.LokerActivity
import com.azwar.uinamfind.ui.magang.MagangActivity
import com.azwar.uinamfind.ui.mahasiswa.MahasiswaActivity
import com.azwar.uinamfind.ui.organisasi.OrganisasiActivity
import com.azwar.uinamfind.ui.pencarian.SearchHomeActivity
import com.azwar.uinamfind.ui.ukm.UKMActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HomeFragment : Fragment() {

    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!

    lateinit var lokerHomeAdapter: LokerHomeAdapter
    lateinit var lokerModel: List<LokerModel>

    lateinit var beasiswaTerbaruAdapter: BeasiswaTerbaruAdapter
    lateinit var beasiswaModel: List<BeasiswaModel>

    private lateinit var cardMahasiswaAdapter: CardMahasiswaAdapter
    private lateinit var mahasiswa: List<Mahasiswa>

    private lateinit var bottomSheetBehaviorMenuLainnya: BottomSheetBehavior<FrameLayout>

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        // Bottom Sheet Menu
        bottomSheetBehaviorMenuLainnya =
            BottomSheetBehavior.from(homeBinding.continerBottomSheetMenuHome)

        homeBinding.rlMahasiswaBottomSheetMenuHome.setOnClickListener {
            bottomSheetBehaviorMenuLainnya.setState(BottomSheetBehavior.STATE_HIDDEN)
            val intent_mahasiswa = Intent(context, MahasiswaActivity::class.java)
            startActivity(intent_mahasiswa)

        }

        homeBinding.rlLembagaBottomSheetMenuHome.setOnClickListener {
            bottomSheetBehaviorMenuLainnya.setState(BottomSheetBehavior.STATE_HIDDEN)
            val intent_lembaga = Intent(context, LembagaActivity::class.java)
            startActivity(intent_lembaga)
        }

        homeBinding.llMenuLainnyaHome.setOnClickListener {

//            MenuHomeBottomSheetDialogFragment().apply {
//                show(parentFragmentManager, "TAG")
//            }
            val state =
                if (bottomSheetBehaviorMenuLainnya.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehaviorMenuLainnya.state = state
        }

        homeBinding.llMenuMahasiswaHome.setOnClickListener {
            val intent_mahasiswa = Intent(context, MahasiswaActivity::class.java)
            startActivity(intent_mahasiswa)
        }

        homeBinding.llMenuLembagaHome.setOnClickListener {
            val intent_lembaga = Intent(context, LembagaActivity::class.java)
            startActivity(intent_lembaga)
        }

        homeBinding.llMenuUkmHome.setOnClickListener {
            val inten_ukm = Intent(context, UKMActivity::class.java)
            startActivity(inten_ukm)
        }

        homeBinding.llMenuOrganisasiHome.setOnClickListener {
            val inten_organisasi = Intent(context, OrganisasiActivity::class.java)
            startActivity(inten_organisasi)
        }

        homeBinding.llMenuInfoMagangHome.setOnClickListener {
            val inten_magang = Intent(context, MagangActivity::class.java)
            startActivity(inten_magang)
        }

        homeBinding.llMenuInfoBeasiswaHome.setOnClickListener {
            val inten_beasiswa = Intent(context, BeasiswaActivity::class.java)
            startActivity(inten_beasiswa)
        }

        homeBinding.llMenuInfoLokerHome.setOnClickListener {
            val inten_loker = Intent(context, LokerActivity::class.java)
            startActivity(inten_loker)
        }

        homeBinding.tvLihatSemuaBeasiswaTerbaru.setOnClickListener {
            val inten_beasiswa = Intent(context, BeasiswaActivity::class.java)
            startActivity(inten_beasiswa)
        }

        homeBinding.tvLihatSemuaRekomendasiLoker.setOnClickListener {
            val intent_loker = Intent(context, LokerActivity::class.java)
            startActivity(intent_loker)
        }

        homeBinding.rlSearchHome.setOnClickListener {
            val intent_search_home = Intent(context, SearchHomeActivity::class.java)
            startActivity(intent_search_home)
        }


        // list profile terbaru
        val layoutManagerProfileTerbaru: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeBinding.rvProfilTerbaruHome.layoutManager = layoutManagerProfileTerbaru
        cardMahasiswaAdapter = CardMahasiswaAdapter()
        homeBinding.rvProfilTerbaruHome.adapter = cardMahasiswaAdapter

        // List rekomendasi loker
        val layoutManagerRekomendasiLoker: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeBinding.rvRekomendasiLokerHome.layoutManager = layoutManagerRekomendasiLoker
        lokerHomeAdapter = LokerHomeAdapter()
        homeBinding.rvRekomendasiLokerHome.adapter = lokerHomeAdapter

        // List Beasiswa Terbaru
        val linearManagerBeasiswaTerbaru: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeBinding.rvBeasiswaTerbaruHome.layoutManager = linearManagerBeasiswaTerbaru
        beasiswaTerbaruAdapter = BeasiswaTerbaruAdapter()
        homeBinding.rvBeasiswaTerbaruHome.adapter = beasiswaTerbaruAdapter

        return homeBinding.root
    }

}