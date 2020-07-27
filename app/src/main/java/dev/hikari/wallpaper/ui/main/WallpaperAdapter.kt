package dev.hikari.wallpaper.ui.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper
import dev.hikari.wallpaper.utils.DensityUtils
import kotlinx.android.synthetic.main.item_wallpaper.view.*

class WallpaperAdapter constructor(
    private val wallpapers: MutableList<Wallpaper>,
    private val onWallpaperClickListener: (Int, View) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.DataViewHolder>() {

    companion object {
        private const val MIN_HEIGHT = 360
        private const val MAX_HEIGHT = 800
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: Wallpaper) {
            val layoutParams = itemView.ivWallpaper.layoutParams
            val imageWidth =
                (itemView.context.resources.displayMetrics.widthPixels - DensityUtils.dp2px(8f) * 3) / 2
            layoutParams.width = imageWidth
            if (layoutParams.height < MIN_HEIGHT) {
                layoutParams.height = MIN_HEIGHT
            } else if (layoutParams.height > MAX_HEIGHT) {
                layoutParams.height = MAX_HEIGHT
            }
            layoutParams.height = layoutParams.width * wallpaper.dimensionY / wallpaper.dimensionX
            itemView.ivWallpaper.layoutParams = layoutParams
            itemView.setBackgroundColor(Color.parseColor(wallpaper.colors[0]))
            Glide.with(itemView.ivWallpaper.context)
                .load(wallpaper.path)
                .into(itemView.ivWallpaper)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        )

    override fun getItemCount(): Int = wallpapers.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.apply {
            bind(wallpapers[position])
            itemView.setOnClickListener {
                onWallpaperClickListener.invoke(position, itemView)
            }
        }
    }

}