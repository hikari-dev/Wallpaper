package dev.hikari.wallpaper.data.network

import dev.hikari.wallpaper.model.WallpaperResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class WallpaperClient @Inject constructor(
    private val wallpaperService: WallpaperService
) {

    suspend fun getWallpapers(page: Int): Response<WallpaperResponse> =
        wallpaperService.getWallpapers(page)

    suspend fun downloadWallpaper(url: String): Response<ResponseBody> =
        wallpaperService.downloadWallpaper(url)
}