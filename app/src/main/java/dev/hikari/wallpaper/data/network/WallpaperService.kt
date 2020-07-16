package dev.hikari.wallpaper.data.network

import dev.hikari.wallpaper.model.WallpaperResponse
import retrofit2.Response
import retrofit2.http.GET

interface WallpaperService {

    @GET
    suspend fun getWallpapers(): Response<WallpaperResponse>
}