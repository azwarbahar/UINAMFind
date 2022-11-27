package com.azwar.uinamfind.ui.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Chatting
import com.azwar.uinamfind.data.models.RoomChat
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityRoomChatBinding
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import com.fasilthottathil.simplechatview.model.ChatMessage
import com.fasilthottathil.simplechatview.widget.SimpleChatView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class RoomChatActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private lateinit var roomChat: RoomChat

    private lateinit var userTo: User
    private var to_user_id: String = ""
    private var to_user_photo: String = ""
    private var to_user_username: String = ""

    private lateinit var userFrom: User
    private var from_user_id: String = ""
    private var from_user_photo: String = ""
    private var from_user_username: String = ""

    private lateinit var chatting: List<Chatting>

    private lateinit var binding: ActivityRoomChatBinding

    private lateinit var simpleChatView: SimpleChatView


    var rootRef = FirebaseDatabase.getInstance().reference
    var usersRef = rootRef.child("Chat")

    private var firstOpenChat = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        userTo = intent.getParcelableExtra("mahasiswa")!!
        simpleChatView = binding.continerChat
        initData(userTo)

        binding.imgBack.setOnClickListener { finish() }

        simpleChatView.setOnMessageSendListener { message ->
            simpleChatView.addMessage(
                ChatMessage(
                    UUID.randomUUID().toString(),//message id
                    message,//message
                    from_user_username,//username
                    from_user_photo,//profile url
                    true,//for setting left or right position of the chat
                    System.currentTimeMillis(),//timestamp
                    SimpleChatView.TYPE_TEXT//message type)
                )
            )
            buatPesanSaya(from_user_id, to_user_id, message)
            startSendDataPesan(message, from_user_id, to_user_id, "Text")
        }

        simpleChatView.setOnChatUserImageClickListener {
            val intent =
                Intent(this, DetailMahasiswaActivity::class.java)
            intent.putExtra("mahasiswa", userTo)
            startActivity(intent)
        }


        simpleChatView.setOnSelectImageClickListener {
            SweetAlertDialog(
                this,
                SweetAlertDialog.WARNING_TYPE
            )
                .setTitleText("Coming soon..")
                .show()
        }

        simpleChatView.setOnSelectVideoClickListener {
            SweetAlertDialog(
                this,
                SweetAlertDialog.WARNING_TYPE
            )
                .setTitleText("Coming soon..")
                .show()
        }

        simpleChatView.setOnSelectCameraClickListener {
            SweetAlertDialog(
                this,
                SweetAlertDialog.WARNING_TYPE
            )
                .setTitleText("Coming soon..")
                .show()
        }

        binding.imgPhoto.setOnClickListener {
            val intent =
                Intent(this, DetailMahasiswaActivity::class.java)
            intent.putExtra("mahasiswa", userTo)
            startActivity(intent)
        }

        binding.tvUserame.setOnClickListener {
            val intent =
                Intent(this, DetailMahasiswaActivity::class.java)
            intent.putExtra("mahasiswa", userTo)
            startActivity(intent)
        }
        usersRef.child(to_user_id).child(from_user_id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var pesan = snapshot.child("pesan").value.toString()
                    if (pesan.equals("null")) {

                    } else {
                        simpleChatView.addMessage(
                            ChatMessage(
                                UUID.randomUUID().toString(),//message id
                                pesan.toString(),//message
                                to_user_username,//username
                                to_user_photo,//profile url
                                false,//for setting left or right position of the chat
                                System.currentTimeMillis(),//timestamp
                                SimpleChatView.TYPE_TEXT//message type)
                            )
                        )
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


//        Toast.makeText(this, System.currentTimeMillis().toString(), Toast.LENGTH_LONG).show()

    }

    private fun buatPesanSaya(fromId: String, toId: String, pesan: String) {
        usersRef.child(fromId).child(toId).child("pesan").setValue(pesan)
            .addOnCompleteListener(OnCompleteListener<Void?> {
//                Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
            })
            .addOnFailureListener {
//                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun startSendDataPesan(
        message: String,
        fromUserId: String,
        toUserId: String,
        tipe_chat: String
    ) {

        ApiClient.instances.addChat(message, fromUserId, toUserId, tipe_chat)
            ?.enqueue(object : Callback<Responses.ResponseChatting> {
                override fun onResponse(
                    call: Call<Responses.ResponseChatting>,
                    response: Response<Responses.ResponseChatting>
                ) {

                }

                override fun onFailure(call: Call<Responses.ResponseChatting>, t: Throwable) {

                }

            })


    }

    private fun initData(user: User) {
        to_user_id = user.id.toString()
        from_user_id = id

        loadMahasiswa("from", from_user_id)
        loadMahasiswa("to", to_user_id)

        laodChatting(from_user_id, to_user_id)

    }

    private fun laodChatting(fromUserId: String, toUserId: String) {

        ApiClient.instances.getChat(fromUserId, toUserId)
            ?.enqueue(object : Callback<Responses.ResponseChatting> {
                override fun onResponse(
                    call: Call<Responses.ResponseChatting>,
                    response: Response<Responses.ResponseChatting>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            chatting = response.body()?.chat_data!!
                            if (chatting.size > 0) {
                                initChat(chatting)
                            }

                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseChatting>, t: Throwable) {

                }

            })

    }

    private fun initChat(chatting: List<Chatting>) {
        chatting.forEach {

//            val givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val mDate: Date = sdf.parse(it.created_at)
            val timeInMilliseconds = mDate.time
//            val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            val date = LocalDate.parse(it.created_at , firstApiFormat)
            if (id.equals(it.from_user.toString())) {
                simpleChatView.addMessage(
                    ChatMessage(
                        it.id.toString(),//message id
                        it.pesan.toString(),//message
                        from_user_username,//username
                        from_user_photo,//profile url
                        true,//for setting left or right position of the chat
                        timeInMilliseconds,//timestamp
                        SimpleChatView.TYPE_TEXT//message type)
                    )
                )
            } else {
                simpleChatView.addMessage(
                    ChatMessage(
                        it.id.toString(),//message id
                        it.pesan.toString(),//message
                        to_user_username,//username
                        to_user_photo,//profile url
                        false,//for setting left or right position of the chat
                        timeInMilliseconds,//timestamp
                        SimpleChatView.TYPE_TEXT//message type)
                    )
                )
            }
        }
    }


    private fun loadMahasiswa(s: String?, mahasiswaId: String) {

        ApiClient.instances.getMahasiswaID(mahasiswaId)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (s.equals("to")) {
                            userTo = response.body()?.result_mahasiswa!!
                            var nim = userTo.nim.toString()

                            // nama
                            var nama_depan = userTo.nama_depan.toString()
                            var nama_belakang = userTo.nama_belakang.toString()
                            var text_nama_lengkap = binding.tvUserame
                            if (nama_depan.equals("null") || nama_depan.equals("")) {
                                text_nama_lengkap.text = nim
                                to_user_username = nim
                            } else if (nama_belakang.equals("null") || nama_belakang.equals("")) {
                                var nama_lengkap = userTo.nama_depan.toString()
                                text_nama_lengkap.text = nama_lengkap
                                to_user_username = nama_lengkap
                            } else {
                                var nama_lengkap =
                                    userTo.nama_depan.toString() + " " + userTo.nama_belakang.toString()
                                text_nama_lengkap.text = nama_lengkap
                                to_user_username = nama_lengkap
                            }
                            val foto = userTo.foto.toString()
                            if (foto.equals("-") || foto.isEmpty()) {
                            } else {
                                to_user_photo = BuildConfig.BASE_URL + "/upload/photo/" + foto
                                Glide.with(this@RoomChatActivity)
                                    .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                                    .into(binding.imgPhoto)
                            }
                        } else if (s.equals("from")) {
                            userFrom = response.body()?.result_mahasiswa!!
                            var nim = userFrom.nim.toString()

                            // nama
                            var nama_depan = userFrom.nama_depan.toString()
                            var nama_belakang = userFrom.nama_belakang.toString()
//                            var text_nama_lengkap = binding.tvUserame
                            if (nama_depan.equals("null") || nama_depan.equals("")) {
//                                text_nama_lengkap.text = nim
                                from_user_username = nim
                            } else if (nama_belakang.equals("null") || nama_belakang.equals("")) {
                                var nama_lengkap = userFrom.nama_depan.toString()
//                                text_nama_lengkap.text = nama_lengkap
                                from_user_username = nama_lengkap
                            } else {
                                var nama_lengkap =
                                    userFrom.nama_depan.toString() + " " + userFrom.nama_belakang.toString()
//                                text_nama_lengkap.text = nama_lengkap
                                from_user_username = nama_lengkap
                            }
                            val foto = userFrom.foto.toString()
                            if (foto.equals("-") || foto.isEmpty()) {
                            } else {
                                from_user_photo = BuildConfig.BASE_URL + "/upload/photo/" + foto
//                                Glide.with(this@RoomChatActivity)
//                                    .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
//                                    .into(binding.imgPhoto)
                            }
                        }

                    } else {
                        if (s.equals("to")) {
                            binding.tvUserame.text = "Username"
                        }
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseMahasiswa>,
                    t: Throwable
                ) {
                    if (s.equals("to")) {
                        binding.tvUserame.text = "Username"
                    }

                }

            })


    }

}