package com.azwar.uinamfind.ui.saya.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.KeahlianMahasiswa
import com.azwar.uinamfind.databinding.ItemKeahlianMahasiswaEditBinding
import com.azwar.uinamfind.ui.saya.keahlian.EditKeahlianMahasiswaActivity

class ListKeahlianMahasiswaAdapter(private val list: List<KeahlianMahasiswa>) :
    RecyclerView.Adapter<ListKeahlianMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemKeahlianMahasiswaEditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: KeahlianMahasiswa) {
            with(itemView) {
                binding.tvNamaItemKeahlian.setText(get.nama_skill)
                var level_skill = get.level_skill
                binding.tvNamaLevelSkill.setText("Level : " + level_skill)
                val progressBar_level = binding.progressLevel
                if (level_skill.equals("Beginner")) {
                    progressBar_level.progress = 1
                } else if (level_skill.equals("Advanced")) {
                    progressBar_level.progress = 2
                } else if (level_skill.equals("Competent")) {
                    progressBar_level.progress = 3
                } else if (level_skill.equals("Proficient")) {
                    progressBar_level.progress = 4
                } else if (level_skill.equals("Expert")) {
                    progressBar_level.progress = 5
                } else {
                    progressBar_level.progress = 1
                }

                binding.imgEditItemKeahlian.setOnClickListener {
                    val intent = Intent(context, EditKeahlianMahasiswaActivity::class.java)
                    intent.putExtra("keahlian", get)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = ItemKeahlianMahasiswaEditBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListKeahlianMahasiswaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}