package com.azwar.uinamfind.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.RoomChat
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityChatBinding
import com.azwar.uinamfind.ui.mahasiswa.MahasiswaActivity
import com.azwar.uinamfind.ui.saya.adapter.PendidikanMahasiswaAdapter
import com.azwar.uinamfind.ui.sosmed.ListSosmedMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityChatBinding

    private lateinit var roomChat: List<RoomChat>

    private lateinit var swipe_refresh: SwipeRefreshLayout

    private lateinit var roomChattingAdapter: RoomChattingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            loadData()
        })

        binding.imgBackChat.setOnClickListener { finish() }

        binding.fabMahasiswa.setOnClickListener {
            val intent = Intent(this, MahasiswaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadData() {

        ApiClient.instances.getRoomChat(id)?.enqueue(object : Callback<Responses.ResponseChatting> {
            override fun onResponse(
                call: Call<Responses.ResponseChatting>,
                response: Response<Responses.ResponseChatting>
            ) {
                swipe_refresh.isRefreshing = false
                if (response.isSuccessful) {
                    var kode = response.body()?.kode
                    var pesan = response.body()?.pesan
                    if (kode.equals("1")) {
                        roomChat = response.body()?.room_data!!
                        if (roomChat.size > 0) {
                            binding.llDataKosong.visibility = View.GONE
                            binding.rvChat.visibility = View.VISIBLE

                            binding.rvChat.layoutManager = LinearLayoutManager(this@ChatActivity)
                            roomChattingAdapter = RoomChattingAdapter(roomChat)

                            val dividerItemDecoration: RecyclerView.ItemDecoration =
                                DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        this@ChatActivity!!, R.drawable.divider
                                    )
                                )
                            binding.rvChat.addItemDecoration(dividerItemDecoration)
                            binding.rvChat.adapter = roomChattingAdapter


                        } else {
                            binding.llDataKosong.visibility = View.VISIBLE
                            binding.rvChat.visibility = View.GONE
                        }
                    } else {
                        binding.llDataKosong.visibility = View.VISIBLE
                        binding.rvChat.visibility = View.GONE
                    }

                } else {
                    binding.llDataKosong.visibility = View.VISIBLE
                    binding.rvChat.visibility = View.GONE
                }


            }

            override fun onFailure(call: Call<Responses.ResponseChatting>, t: Throwable) {
                swipe_refresh.isRefreshing = false
                binding.llDataKosong.visibility = View.VISIBLE
                binding.rvChat.visibility = View.GONE
            }

        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onRefresh() {
        loadData()
    }
}