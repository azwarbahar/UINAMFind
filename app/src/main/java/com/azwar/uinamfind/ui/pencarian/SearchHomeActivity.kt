package com.azwar.uinamfind.ui.pencarian

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.azwar.uinamfind.R
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.ui.pencarian.adapter.ViewPagerSearchAdapter
import com.azwar.uinamfind.ui.pencarian.fragment.MahasiswaSearchFragment
import com.azwar.uinamfind.utils.Constanta
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_search_home.*


class SearchHomeActivity : AppCompatActivity() {

    private lateinit var searchView: SimpleSearchView

    private lateinit var sharedPref: PreferencesHelper

    var mPencarianInterfacr: PencarianInterfacr? = null
    fun setPencarianUpdateListener(pencarianInterfacr: PencarianInterfacr) {
        mPencarianInterfacr = pencarianInterfacr
    }

    val tabArray = arrayOf(
        "Mahasiswa",
        "Loker",
        "Beasiswa",
        "Magang",
        "Lembaga",
        "UKM",
        "Organisasi"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_home)
        sharedPref = PreferencesHelper(this)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        searchView = findViewById<View>(R.id.searchView) as SimpleSearchView
//        searchView.setTabLayout(findViewById(R.id.tabLayout_pencarian));
        toolbar.setTitleTextColor(Color.WHITE);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        setSupportActionBar(toolbar)
        setupTabFragment()

        searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
//                if (query.length > 2) {
                Log.d("SimpleSearchView", "Submit:$query")
//                    Toast.makeText(this@SearchHomeActivity, "Text Submit:$query", Toast.LENGTH_SHORT)
//                        .show()
//                }
                setupShared(query)
                mPencarianInterfacr?.updateQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("SimpleSearchView", "Text changed:$newText")
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                Log.d("SimpleSearchView", "Text cleared")
                return false
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (searchView.onActivityResult(requestCode, resultCode, data!!)) {

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun serFragmentChange(newText: String) {

        supportFragmentManager.findFragmentById(R.id.fragment_search_mahasiswa)

        var frag1 = MahasiswaSearchFragment()

//        frag1.add(R.id.container, conv);

    }

    private fun setupShared(query: String) {
        sharedPref.put(Constanta.QUERY_SEARCH, query)
    }

    private fun setupTabFragment() {

        val viewPager = view_pager_pencarian
        val tabLayout = tabLayout_pencarian

        val adapter = ViewPagerSearchAdapter(supportFragmentManager, lifecycle, 7)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

//        val item: MenuItem = menu!!.findItem(R.id.action_search)
//        searchView.setMenuItem(item)
        searchView.showSearch()
        searchView.enableVoiceSearch(true)



        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_search -> {
                searchView.showSearch()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (searchView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

}