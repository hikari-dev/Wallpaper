package dev.hikari.wallpaper.model

import com.squareup.moshi.Json

data class WallpaperResponse(val data: List<Wallpaper>, val metaData: Metadata)

data class Wallpaper(
    val id: String,
    val url: String,
    val source: String,
    val category: String,
    val purity: String,
    val path: String,
    val views: Int,
    val favorites: Int,
    @Json(name = "dimension_x")
    val dimensionX: Int,
    @Json(name = "dimension_y")
    val dimensionY: Int,
    val resolution: String,
    @Json(name = "created_at")
    val createdAt: String,
    val thumbs: Thumbs
)

data class Thumbs(
    val large: String,
    val original: String,
    val small: String
)

data class Metadata(
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "last_page")
    val lastPage: Int,
    @Json(name = "per_page")
    val perPage: Int,
    @Json(name = "total")
    val total: Int
)