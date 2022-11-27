package com.azwar.uinamfind.ui.lembaga.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.databinding.ItemIconSosmedBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class SosmedLembagaAdapter(private val list: List<Sosmed>) :
    RecyclerView.Adapter<SosmedLembagaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemIconSosmedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Sosmed) {

            with(itemView) {

                var nama = get.nama_sosmed
                var link_sosmed = get.url_sosmed
                if (nama.equals("Facebook")) {
                    setImageSosmed(nama, link_sosmed, 0, itemView)
                } else if (nama.equals("Instagram")) {
                    setImageSosmed(nama, link_sosmed, 1, itemView)
                } else if (nama.equals("Whatsapp")) {
                    setImageSosmed(nama, link_sosmed, 2, itemView)
                } else if (nama.equals("Twitter")) {
                    setImageSosmed(nama, link_sosmed, 3, itemView)
                } else if (nama.equals("TikTok")) {
                    setImageSosmed(nama, link_sosmed, 4, itemView)
                } else if (nama.equals("Youtube")) {
                    setImageSosmed(nama, link_sosmed, 5, itemView)
                } else if (nama.equals("Linkedin")) {
                    setImageSosmed(nama, link_sosmed, 6, itemView)
                } else if (nama.equals("Github")) {
                    setImageSosmed(nama, link_sosmed, 7, itemView)
                } else if (nama.equals("Spotify")) {
                    setImageSosmed(nama, link_sosmed, 8, itemView)
                } else if (nama.equals("Telegram")) {
                    setImageSosmed(nama, link_sosmed, 9, itemView)
                } else if (nama.equals("Website")) {
                    setImageSosmed(nama, link_sosmed, 10, itemView)
                } else {
                    Glide.with(itemView)
                        .load(R.drawable.circle_primary_trans_10)
                        .into(binding.imgSosmed)
                }

                itemView.setOnClickListener {
                    if (link_sosmed.equals("-") || link_sosmed.equals("")) {
                        Toast.makeText(context, "Link tidak terdeteksi", Toast.LENGTH_SHORT)
                    } else {
                        val defaultBrowser = Intent.makeMainSelectorActivity(
                            Intent.ACTION_MAIN,
                            Intent.CATEGORY_APP_BROWSER
                        )
                        defaultBrowser.data = Uri.parse(link_sosmed)
                        context.startActivity(defaultBrowser)
                    }
                }

            }

        }

        private fun setImageSosmed(nama: String?, link_sosmed: String?, i: Int, itemView: View) {
            var iconSosmed = arrayOf(
                R.drawable.ic_sosmed_facebook,
                R.drawable.ic_sosmed_instagram,
                R.drawable.ic_sosmed_whatsapp,
                R.drawable.ic_sosmed_twitter,
                R.drawable.ic_sosmed_tiktok,
                R.drawable.ic_sosmed_youtube,
                R.drawable.ic_sosmed_linkedin,
                R.drawable.ic_sosmed_github,
                R.drawable.ic_sosmed_spotify,
                R.drawable.ic_sosmed_telegram,
                R.drawable.ic_browser
            );
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(iconSosmed[i])
            Glide.with(itemView)
                .load(link_sosmed)
                .apply(options)
                .into(binding.imgSosmed)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        var binding =
            ItemIconSosmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SosmedLembagaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}