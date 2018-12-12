package com.waracle.androidtest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_layout.view.*


class CakeAdapter : RecyclerView.Adapter<CakeAdapter.CakeHolder>() {

    var data: MutableList<Cake> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CakeHolder(inflater, parent, R.layout.list_item_layout)
    }

    override fun onBindViewHolder(holder: CakeHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class CakeHolder(private val inflater: LayoutInflater, private val parent: ViewGroup, private val layoutResId: Int) :
            RecyclerView.ViewHolder(inflater.inflate(layoutResId, parent, false)) {

        fun bind(cake: Cake) {
            itemView.apply {
                title_tv.text = cake.title
                desc_tv.text = cake.description
                image_iv.setImageBitmap(ImageLoader.loadBitmapFromUrl(cake.imageUrl))
            }
        }

    }
}

