package com.azwar.uinamfind.ui.mahasiswa

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.mahasiswa.adapter.KeahlianAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.OrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.PendidikanAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.PengalamanAdapter
import java.io.File
import java.io.FileOutputStream
import java.util.*


class DetailMahasiswaActivity : AppCompatActivity() {

    private lateinit var keahlianAdapter: KeahlianAdapter

    private lateinit var pengalamanAdapter: PengalamanAdapter

    private lateinit var pendidikanAdapter: PendidikanAdapter

    private lateinit var organisasiMahasiswaAdapter: OrganisasiMahasiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mahasiswa)

        val cv_card_mahasiswa: CardView = findViewById(R.id.cv_card_detail_mahasiswa)
        cv_card_mahasiswa.setOnClickListener {
//            cv_card_mahasiswa.setDrawingCacheEnabled(true)
//            val b: Bitmap = cv_card_mahasiswa.getDrawingCache()
//            SaveImage(b)
//            b.compress(CompressFormat.JPEG, 100, FileOutputStream("/some/location/image.jpg"))
        }

        val img_bacl_logo_card: ImageView
        img_bacl_logo_card = findViewById(R.id.img_bacl_logo_card);
        img_bacl_logo_card.alpha = 0.1f

        val rv_pengalaman_detail_mahasiswa: RecyclerView =
            findViewById(R.id.rv_pengalaman_detail_mahasiswa)
        rv_pengalaman_detail_mahasiswa.layoutManager = LinearLayoutManager(this)
        pengalamanAdapter = PengalamanAdapter()
        rv_pengalaman_detail_mahasiswa.adapter = pengalamanAdapter


        val rv_keahlian_detail_mahasiswa: RecyclerView =
            findViewById(R.id.rv_keahlian_detail_mahasiswa)
        rv_keahlian_detail_mahasiswa.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        keahlianAdapter = KeahlianAdapter()
        rv_keahlian_detail_mahasiswa.adapter = keahlianAdapter

        val rv_pendidikan_detail_mahasiswa: RecyclerView =
            findViewById(R.id.rv_pendidikan_detail_mahasiswa)
        rv_pendidikan_detail_mahasiswa.layoutManager = LinearLayoutManager(this)
        pendidikanAdapter = PendidikanAdapter()
        rv_pendidikan_detail_mahasiswa.adapter = pendidikanAdapter

        val rv_organisasi_mahasiswa_detail_mahasiswa: RecyclerView =
            findViewById(R.id.rv_organisasi_mahasiswa_detail_mahasiswa)
        rv_organisasi_mahasiswa_detail_mahasiswa.layoutManager = LinearLayoutManager(this)
        organisasiMahasiswaAdapter = OrganisasiMahasiswaAdapter()
        rv_organisasi_mahasiswa_detail_mahasiswa.adapter = organisasiMahasiswaAdapter

    }

    private fun SaveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            Toast.makeText(this, "sukses", Toast.LENGTH_SHORT)
            val out: FileOutputStream = FileOutputStream(file)
            finalBitmap.compress(CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT)
            e.printStackTrace()
        }
    }

}