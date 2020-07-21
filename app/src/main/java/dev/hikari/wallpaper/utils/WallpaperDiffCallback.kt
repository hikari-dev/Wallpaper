package dev.hikari.wallpaper.utils

import androidx.recyclerview.widget.DiffUtil
import dev.hikari.wallpaper.model.Wallpaper

class WallpaperDiffCallback(
    private val oldData: List<Wallpaper>,
    private val newData: List<Wallpaper>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition].id == newData[newItemPosition].id

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition] == newData[newItemPosition]
}