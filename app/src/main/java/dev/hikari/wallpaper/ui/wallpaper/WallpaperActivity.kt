package dev.hikari.wallpaper.ui.wallpaper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.data.network.WallpaperClient
import dev.hikari.wallpaper.model.Wallpaper
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.android.synthetic.main.layout_detail.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File
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
                .into(photoView)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomScrollView)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                Timber.d("bottomSheetBehavior slideOffset -> $slideOffset")
                val deltaY = bottomScrollView.height - bottomSheetBehavior.peekHeight
                photoView.translationY = -slideOffset * 0.7f * deltaY
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })

        tvDownload.setOnClickListener {
            lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
                Timber.e("download exception:${throwable.message}")
                Toast.makeText(this@WallpaperActivity, "下载失败TAT", Toast.LENGTH_SHORT).show()
            }) {
                val response = wallpaperClient.downloadWallpaper(wallpaper.path)
                if (response.isSuccessful) {
                    val filename = wallpaper.path.substringAfterLast("/")
                    saveFile(filename, response.body()!!)
                    Toast.makeText(this@WallpaperActivity, "下载完成", Toast.LENGTH_SHORT).show()
                } else {
                    Timber.e("download failed")
                }
            }
        }

        tvShare.setOnClickListener {
            Toast.makeText(this, "功能待开发中...", Toast.LENGTH_SHORT).show()
        }

        tvSetWallpaper.setOnClickListener {
            val wallpaperManager = WallpaperManager.getInstance(this)
            Glide.with(this).load(wallpaper.path).into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    // TODO: 2020/7/31 use setBitmap(Bitmap fullImage, Rect visibleCropHint, boolean allowBackup) to control wallpaper rect
                    wallpaperManager.setBitmap((resource as BitmapDrawable).bitmap)
                    Toast.makeText(this@WallpaperActivity, "设置壁纸成功", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    @Suppress("DEPRECATION")
    private suspend fun saveFile(fileName: String, body: ResponseBody) =
        withContext(Dispatchers.IO) {
            val dir =
                File("${Environment.getExternalStorageDirectory().absolutePath}/${Environment.DIRECTORY_PICTURES}/x")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            File(dir, fileName).writeBytes(body.use { it.bytes() })
        }

}