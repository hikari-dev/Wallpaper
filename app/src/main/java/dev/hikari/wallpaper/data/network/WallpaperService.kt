package dev.hikari.wallpaper.data.network

import dev.hikari.wallpaper.model.WallpaperResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperService {

    @GET("search")
    suspend fun getWallpapers(
        @Query("page") page: Int
    ): Response<WallpaperResponse>
}