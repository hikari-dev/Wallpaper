package dev.hikari.wallpaper.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_WALLPAPER = "EXTRA_WALLPAPER"

        fun startActivity(context: Context, wallpaper: Wallpaper) {
            // TODO: 2020/7/22 添加过渡动画
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_WALLPAPER, wallpaper)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: 2020/7/22 UI设计
        setContentView(R.layout.activity_detail)
        val wallpaper: Wallpaper = requireNotNull(intent.getParcelableExtra(EXTRA_WALLPAPER))
    }


}