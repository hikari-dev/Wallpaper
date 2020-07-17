package dev.hikari.wallpaper.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper
import kotlinx.android.synthetic.main.item_wallpaper.view.*

class WallpaperAdapter(
    private val wallpapers: ArrayList<Wallpaper>
) :
    RecyclerView.Adapter<WallpaperAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpaper: Wallpaper) {
            itemView.ivWallpaper.load(wallpaper.path) {
                crossfade(true)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        )

    override fun getItemCount(): Int = wallpapers.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(wallpapers[position])

    fun addData(list: List<Wallpaper>) {
        wallpapers.addAll(list)
    }
}