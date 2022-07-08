package com.azwar.uinamfind.ui.saya.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.PendidikanMahasiswa
import com.azwar.uinamfind.databinding.ItemPendidikanEditBinding
import com.azwar.uinamfind.ui.saya.organisasi.EditOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pendidikan.EditPendidikanMahasiswaActivity
import java.text.SimpleDateFormat

class ListPendidikanMahasiswaAdapter(private val list: List<PendidikanMahasiswa>) :
    RecyclerView.Adapter<ListPendidikanMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemPendidikanEditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: PendidikanMahasiswa) {
            with(itemView) {
                var gelar = get.gelar
                if (gelar == null) {
                    binding.tvNamaJurusanPendidikan.setText(get.jurusan)
                } else {
                    binding.tvNamaJurusanPendidikan.setText(gelar + " - " + get.jurusan)
                }
                binding.tvNamaSekolahPendidikan.setText(get.nama_tempat)
                var status_pendidikan = get.status_pendidikan
                var thn_masuk = get.tanggal_masuk
                var thn_berakhir = get.tanggal_berakhir
                if (status_pendidikan.equals("Masih")) {
                    binding.tvTahunMasaPendidikan.setText(convertDate(thn_masuk) + " - Sekarang")
                } else {
                    binding.tvTahunMasaPendidikan.setText(
                        convertDate(thn_masuk) + " - " + convertDate(
                            thn_berakhir
                        )
                    )
                }

                binding.imgEditItemPendidikan.setOnClickListener {
                    val intent = Intent(context, EditPendidikanMahasiswaActivity::class.java)
                    intent.putExtra("pendidikan", get)
                    context.startActivity(intent)
                }

            }
        }

        private fun convertDate(date: String?): String {
            val parser = SimpleDateFormat("dd-MM-yyyy")
            val formatter = SimpleDateFormat("yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = ItemPendidikanEditBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListPendidikanMahasiswaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}