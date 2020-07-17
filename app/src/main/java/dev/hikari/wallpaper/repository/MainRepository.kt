package dev.hikari.wallpaper.repository

import dev.hikari.wallpaper.data.network.WallpaperClient
import dev.hikari.wallpaper.model.WallpaperResponse
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val wallpaperClient: WallpaperClient
) {


    suspend fun fetchWallpapers(page: Int): Response<WallpaperResponse> {
        return wallpaperClient.getWallpapers(page)
    }

}