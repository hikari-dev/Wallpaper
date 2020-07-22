package dev.hikari.wallpaper.model

import com.squareup.moshi.Json

data class WallpaperResponse(val data: List<Wallpaper>, val meta: Meta)

data class Wallpaper(
    val id: String,
    val url: String,
    val source: String,
    val category: String,
    val purity: String,
    val path: String,
    val views: Int,
    val favorites: Int,
    //java里使用@Json,kotlin里则必须使用@field:Json告诉jvm这个annotation是作用在field上的,否则注解无效
    @field:Json(name = "dimension_x") val dimensionX: Int,
    @field:Json(name = "dimension_y") val dimensionY: Int,
    val resolution: String,
    @field:Json(name = "created_at") val createdAt: String,
    val thumbs: Thumbs
)

data class Thumbs(
    val large: String,
    val original: String,
    val small: String
)

data class Meta(
    @field:Json(name = "current_page") val currentPage: Int,
    @field:Json(name = "last_page") val lastPage: Int,
    @field:Json(name = "per_page") val perPage: Int,
    @field:Json(name = "total") val total: Int
)