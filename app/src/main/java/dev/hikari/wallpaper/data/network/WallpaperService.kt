package dev.hikari.wallpaper.data.network

import dev.hikari.wallpaper.model.WallpaperResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WallpaperService {

    @GET("search")
    suspend fun getWallpapers(
        @Query("page") page: Int
    ): Response<WallpaperResponse>

    @GET
    suspend fun downloadWallpaper(
        @Url url: String
    ): Response<ResponseBody>
}