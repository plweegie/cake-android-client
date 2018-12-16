package com.waracle.androidtest

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_layout.view.*
import kotlinx.coroutines.*

class CakeAdapter : RecyclerView.Adapter<CakeAdapter.CakeHolder>() {

    var data: MutableList<Cake> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CakeHolder(inflater, parent, R.layout.list_item_layout)
    }

    override fun onBindViewHolder(holder: CakeHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = position.toLong()

    inner class CakeHolder(inflater: LayoutInflater, parent: ViewGroup, layoutResId: Int) :
            RecyclerView.ViewHolder(inflater.inflate(layoutResId, parent, false)) {

        fun bind(cake: Cake) {

            GlobalScope.launch(Dispatchers.Main) {
                val bitmap = withContext(Dispatchers.Default) {
                    getBitmapFromUrl(cake.imageUrl)
                }

                itemView.apply {
                    title_tv.text = cake.title
                    desc_tv.text = cake.description
                    image_iv.setImageBitmap(bitmap)
                }
            }
        }

        private suspend fun getBitmapFromUrl(url: String): Bitmap = runBlocking {
            return@runBlocking async { ImageLoader.loadBitmapFromUrl(url) }.await()
        }
    }
}

