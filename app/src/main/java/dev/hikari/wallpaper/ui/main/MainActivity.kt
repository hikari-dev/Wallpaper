package dev.hikari.wallpaper.ui.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper
import dev.hikari.wallpaper.ui.wallpaper.WallpaperActivity
import dev.hikari.wallpaper.utils.DensityUtils
import dev.hikari.wallpaper.utils.Status
import dev.hikari.wallpaper.widget.StaggeredItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var mAdapter: WallpaperAdapter
    private val loadMoreThreshold = 2
    private var currentPage = 1
    private var isLoadingMore = false
    private val wallpapers = mutableListOf<Wallpaper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 100
            )
        }
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.apply {
            val staggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            layoutManager = staggeredGridLayoutManager
            mAdapter = WallpaperAdapter(wallpapers) { position, view ->
                WallpaperActivity.startActivity(this@MainActivity, wallpapers[position], view)
            }
            adapter = mAdapter
            addItemDecoration(StaggeredItemDecoration(DensityUtils.dp2px(8.0f)))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPositions =
                        (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(
                            null
                        )
                    val needLoadMore = (lastVisibleItemPositions.maxOrNull()
                        ?: 0) + loadMoreThreshold >= (recyclerView.layoutManager as StaggeredGridLayoutManager).itemCount
                    if (needLoadMore && !isLoadingMore && currentPage != 1) {
                        Timber.e("start loadMore")
                        isLoadingMore = true
                        mainViewModel.fetchWallpapers(currentPage)
                    }
                }
            })
        }

        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            val size = wallpapers.size
            wallpapers.clear()
            mAdapter.notifyItemRangeRemoved(0, size)
            mainViewModel.fetchWallpapers(currentPage)
        }


    }

    private fun setupObserver() {
        mainViewModel.wallpapers.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    isLoadingMore = false
                    currentPage++
                    swipeRefreshLayout.isRefreshing = false
                    it.data?.let { list ->
                        renderList(list)
                    }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    isLoadingMore = false
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    if (currentPage == 1) {
                        swipeRefreshLayout.isRefreshing = true
                    }
                }
            }
        })
    }

    private fun renderList(list: List<Wallpaper>) {
        wallpapers.addAll(list)
        //这里不要用notifyDataSetChanged()方法,不然可能会导致加载多页数据后顶部留白
        mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
    }
}
