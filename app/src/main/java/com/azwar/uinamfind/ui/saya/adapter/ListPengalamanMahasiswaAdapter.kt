package com.azwar.uinamfind.ui.saya.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.PengalamanMahasiswa
import com.azwar.uinamfind.databinding.ItemPengalamanMahasiswaEditBinding
import com.azwar.uinamfind.ui.saya.organisasi.EditOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.EditPengalamanMahasiswaActivity
import com.azwar.uinamfind.utils.ui.MyTextViewDesc
import java.text.SimpleDateFormat

class ListPengalamanMahasiswaAdapter(private val list: List<PengalamanMahasiswa>) :
    RecyclerView.Adapter<ListPengalamanMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemPengalamanMahasiswaEditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: PengalamanMahasiswa) {
            with(itemView) {

                binding.tvJudulItemPengalaman.setText(list.nama)
                binding.tvNamaKantorItemPengalaman.setText(list.nama_tempat + " - " + list.jenis_pengalaman)
                var tgl_mulai = list.tanggal_mulai
                var tgl_berakhir = list.tanggal_berakhir
                var status = list.status_pengalaman
                if (status.equals("Berjalan")) {
                    binding.tvMasaItemPengalaman.setText(convertDate(tgl_mulai) + " - Sekarang")
                } else {
                    binding.tvMasaItemPengalaman.setText(
                        convertDate(tgl_mulai) + " - " + convertDate(
                            tgl_berakhir
                        )
                    )
                }
                var deskripsi = list.deskripsi
                var tv_desc = binding.tvDescItemPengalamanMahasiswa
                if (deskripsi.equals("") || deskripsi!!.isEmpty() || deskripsi == null) {
//                    binding.tvDescItemPengalamanMahasiswa.visibility = View.GONE
                } else {
                    tv_desc.visibility = View.VISIBLE
                    tv_desc.text = deskripsi
//                    if (binding.tvDescItemPengalamanMahasiswa.lineCount > 2) {
                    val myTextViewDesc = MyTextViewDesc()
                    myTextViewDesc.makeTextViewResizable(tv_desc, 2, ".. Lihat lengkap", true)
//                    }
                }

                binding.imgEditItemPengalaman.setOnClickListener {
                    val intent = Intent(context, EditPengalamanMahasiswaActivity::class.java)
                    intent.putExtra("pengalaman", list)
                    context.startActivity(intent)
                }

            }
        }

        private fun convertDate(date: String?): String {
            val parser = SimpleDateFormat("dd-MM-yyyy")
            val formatter = SimpleDateFormat("MMM yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = ItemPengalamanMahasiswaEditBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListPengalamanMahasiswaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}