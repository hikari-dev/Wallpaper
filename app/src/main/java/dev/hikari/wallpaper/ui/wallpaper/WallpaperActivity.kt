package dev.hikari.wallpaper.ui.wallpaper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.data.network.WallpaperClient
import dev.hikari.wallpaper.model.Wallpaper
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.android.synthetic.main.layout_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class WallpaperActivity : AppCompatActivity() {

    @Inject
    lateinit var wallpaperClient: WallpaperClient

    companion object {
        private const val EXTRA_WALLPAPER = "EXTRA_WALLPAPER"

        fun startActivity(context: Activity, wallpaper: Wallpaper, view: View) {
            val intent = Intent(context, WallpaperActivity::class.java)
            intent.putExtra(EXTRA_WALLPAPER, wallpaper)
            val transitionName = context.resources.getString(R.string.transition_name)
            val transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, transitionName)
            context.startActivity(intent, transitionActivityOptions.toBundle())
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper)

        toolbar.setNavigationOnClickListener { finishAfterTransition() }

        val wallpaper: Wallpaper = requireNotNull(intent.getParcelableExtra(EXTRA_WALLPAPER))
        with(wallpaper) {
            tvCreatedAt.text = "上传于${createdAt}"
            tvSize.text = resolution
            tvLikes.text = favorites.toString()
            tvViews.text = views.toString()
            Glide.with(this@WallpaperActivity)
                .load(path)
                .into(ivWallpaper)
        }
        ivWallpaper.setOnClickListener {
            WallpaperDetailActivity.startActivity(this, wallpaper)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomScrollView)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Timber.d("bottomSheetBehavior slideOffset -> $slideOffset")
                val deltaY = bottomScrollView.height - bottomSheetBehavior.peekHeight
                ivWallpaper.translationY = -slideOffset * 0.7f * deltaY
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })

        tvDownload.setOnClickListener {
            lifecycleScope.launch {
                val response = wallpaperClient.downloadWallpaper(wallpaper.path)
                if (response.isSuccessful) {
                    saveFile(response.body()!!)
                } else {
                    Timber.e("download failed")
                }
            }
        }

    }

    private suspend fun saveFile(body: ResponseBody) = withContext(Dispatchers.IO) {

    }

}