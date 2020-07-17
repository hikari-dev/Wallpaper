package dev.hikari.wallpaper.model

import com.squareup.moshi.Json

data class WallpaperResponse(val data: List<Wallpaper>, val meta: Meta)

// TODO: 2020/7/17 用@Json(name = "created_at")的注解会有解析出来是空的问题
data class Wallpaper(
    val id: String,
    val url: String,
    val source: String,
    val category: String,
    val purity: String,
    val path: String,
    val views: Int,
    val favorites: Int,
    val dimension_x: Int,
    val dimension_y: Int,
    val resolution: String,
    val created_at: String,
    val thumbs: Thumbs
)

data class Thumbs(
    val large: String,
    val original: String,
    val small: String
)

data class Meta(
    @Json(name = "current_page") val currentPage: Int,
    @Json(name = "last_page") val lastPage: Int,
    @Json(name = "per_page") val perPage: Int,
    @Json(name = "total") val total: Int
)