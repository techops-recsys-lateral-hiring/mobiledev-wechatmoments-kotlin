package com.tws.moments.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.TWApplication
import com.tws.moments.utils.dip

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ImageHolder>() {
    var onImageClick: ((AppCompatImageView) -> Unit)? = null

    var images: List<String>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val imageView = AppCompatImageView(parent.context).apply {
            this.scaleType = ImageView.ScaleType.CENTER_CROP
            this.layoutParams =
                RecyclerView.LayoutParams(parent.context.dip(94), parent.context.dip(94))
            setOnClickListener {
                onImageClick?.invoke(it as AppCompatImageView)
            }
        }
        return ImageHolder(imageView)
    }

    override fun getItemCount(): Int {
        return images?.size ?: 0
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        (holder.itemView as? AppCompatImageView)?.let {
            val url = images!![position]
            it.tag = url
            TWApplication.imageLoader.displayImage(url, it)
        }
    }

    inner class ImageHolder(view: View) : RecyclerView.ViewHolder(view)
}
