package dev.hikari.wallpaper.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.hikari.wallpaper.R
import dev.hikari.wallpaper.model.Wallpaper
import dev.hikari.wallpaper.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter: WallpaperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = WallpaperAdapter(arrayListOf())
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        mainViewModel.wallpapers.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { wallpapers ->
                        renderList(wallpapers)
                    }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        })
    }

    private fun renderList(wallpapers: List<Wallpaper>) {
        adapter.addData(wallpapers)
        adapter.notifyDataSetChanged()
    }
}