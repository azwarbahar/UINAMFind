package com.azwar.uinamfind.ui.saya.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.OrganisasiMahasiswa
import com.azwar.uinamfind.databinding.ItemOrganisasiMahasiswaEditBinding
import com.azwar.uinamfind.ui.saya.organisasi.EditOrganisasiMahasiswaActivity
import java.text.SimpleDateFormat

class ListOrganisasiMahasiswaAdapter(
    private val list: List<OrganisasiMahasiswa>
) :
    RecyclerView.Adapter<ListOrganisasiMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val itemOrganisasiMahasiswaEditBinding: ItemOrganisasiMahasiswaEditBinding) :
        RecyclerView.ViewHolder(itemOrganisasiMahasiswaEditBinding.root) {

        fun bind(list: OrganisasiMahasiswa) {

            with(itemView) {

                itemOrganisasiMahasiswaEditBinding.tvNamaItemOrganisasi.text = list.nama_organisasi
                itemOrganisasiMahasiswaEditBinding.tvJabatanItemPendidikan.text = list.jabatan
                var tgl_mulai = list.tanggal_mulai
                var tgl_berakhir = list.tanggal_berakhir
                var status = list.status_organisasi_user
                if (status.equals("Berjalan")) {
                    itemOrganisasiMahasiswaEditBinding.tvMasaItemOrganisasi.text =
                        convertDate(tgl_mulai) + " - Sekarang"
                } else {
                    itemOrganisasiMahasiswaEditBinding.tvMasaItemOrganisasi.text =
                        convertDate(tgl_mulai) +
                                " - " + convertDate(tgl_berakhir)
                }
                var deskripsi = list.deskripsi
                var tv_desc = itemOrganisasiMahasiswaEditBinding.tvDescItemOrganisasiMahasiswa
                if (deskripsi == null) {
                    tv_desc.visibility = View.GONE
                } else {
                    tv_desc.visibility = View.VISIBLE
                    tv_desc.text = deskripsi
                }

                itemOrganisasiMahasiswaEditBinding.imgEditItemOrganisasi.setOnClickListener {
                    val intent = Intent(context, EditOrganisasiMahasiswaActivity::class.java)
                    context.startActivity(intent)
                }

            }
        }

        private fun convertDate(date: String): String {
            val parser = SimpleDateFormat("dd-MM-yyyy")
            val formatter = SimpleDateFormat("MMM yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemOrganisasiMahasiswaEditBinding = ItemOrganisasiMahasiswaEditBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )


        return MyHolderView(itemOrganisasiMahasiswaEditBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}