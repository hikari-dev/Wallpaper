package dev.hikari.wallpaper.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper
import kotlinx.android.synthetic.main.item_wallpaper.view.*

class WallpaperAdapter constructor(
    private val wallpapers: MutableList<Wallpaper>,
    private val onWallpaperClickListener: (Int) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.DataViewHolder>() {

    companion object {
        private const val MIN_HEIGHT = 360
        private const val MAX_HEIGHT = 800
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: Wallpaper) {
            val layoutParams = itemView.ivWallpaper.layoutParams
            val imageWidth = itemView.context.resources.displayMetrics.widthPixels / 2
            layoutParams.width = imageWidth
            layoutParams.height = imageWidth * wallpaper.dimensionY / wallpaper.dimensionX
            if (layoutParams.height < MIN_HEIGHT) {
                layoutParams.height = MIN_HEIGHT
            } else if (layoutParams.height > MAX_HEIGHT) {
                layoutParams.height = MAX_HEIGHT
            }
            itemView.ivWallpaper.layoutParams = layoutParams
            Glide.with(itemView.ivWallpaper.context)
                .load(wallpaper.thumbs.original)
                .into(itemView.ivWallpaper)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        )

    override fun getItemCount(): Int = wallpapers.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(wallpapers[position])
        holder.itemView.setOnClickListener { onWallpaperClickListener.invoke(position) }
    }

}