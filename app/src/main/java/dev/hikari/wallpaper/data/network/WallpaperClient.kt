package dev.hikari.wallpaper.data.network

import dev.hikari.wallpaper.model.WallpaperResponse
import retrofit2.Response
import javax.inject.Inject

class WallpaperClient @Inject constructor(
    private val wallpaperService: WallpaperService
) {

    suspend fun getWallpapers(page: Int): Response<WallpaperResponse> =
        wallpaperService.getWallpapers(page)
}