package dev.hikari.wallpaper.ui.wallpaper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper
import kotlinx.android.synthetic.main.activity_wallpaper_detail.*

class WallpaperDetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_WALLPAPER = "EXTRA_WALLPAPER"

        fun startActivity(context: Activity, wallpaper: Wallpaper) {
            val intent = Intent(context, WallpaperDetailActivity::class.java)
            intent.putExtra(EXTRA_WALLPAPER, wallpaper)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper_detail)

        toolbar.setNavigationOnClickListener { finish() }

        val wallpaper: Wallpaper = requireNotNull(intent.getParcelableExtra(EXTRA_WALLPAPER))
        Glide.with(this)
            .load(wallpaper.path)
            .into(photoView)

        tvDownload.setOnClickListener {

        }
    }

}