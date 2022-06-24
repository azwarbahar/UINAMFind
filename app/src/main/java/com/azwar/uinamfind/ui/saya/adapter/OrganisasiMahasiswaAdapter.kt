package com.azwar.uinamfind.ui.saya.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.OrganisasiMahasiswa
import com.azwar.uinamfind.databinding.ItemOrganisasiMahasiswaBinding
import java.text.SimpleDateFormat

class OrganisasiMahasiswaAdapter(private val list: List<OrganisasiMahasiswa>):
    RecyclerView.Adapter<OrganisasiMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val itemOrganisasiMahasiswaBinding: ItemOrganisasiMahasiswaBinding) :
        RecyclerView.ViewHolder(itemOrganisasiMahasiswaBinding.root) {

        fun bind(list: OrganisasiMahasiswa) {

            with(itemView) {

                itemOrganisasiMahasiswaBinding.tvNamaItemOrganisasi.text = list.nama_organisasi
                itemOrganisasiMahasiswaBinding.tvJabatanItemPendidikan.text = list.jabatan
                var tgl_mulai = list.tanggal_mulai
                var tgl_berakhir = list.tanggal_berakhir
                var status = list.status_organisasi_user
                if (status.equals("Berjalan")){
                    itemOrganisasiMahasiswaBinding.tvMasaItemOrganisasi.text = convertDate(tgl_mulai) + " - Sekarang"
                } else {
                    itemOrganisasiMahasiswaBinding.tvMasaItemOrganisasi.text = convertDate(tgl_mulai) +
                            " - " + convertDate(tgl_berakhir)
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
        val itemOrganisasiMahasiswaBinding = ItemOrganisasiMahasiswaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )


        return MyHolderView(itemOrganisasiMahasiswaBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(list.get(position))

    override fun getItemCount() = list.size
}