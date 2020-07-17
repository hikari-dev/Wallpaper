package dev.hikari.wallpaper.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hikari.wallpaper.model.Wallpaper
import dev.hikari.wallpaper.repository.MainRepository
import dev.hikari.wallpaper.utils.NetworkUtils
import dev.hikari.wallpaper.utils.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    val wallpapers = MutableLiveData<Resource<List<Wallpaper>>>()

    init {
        Timber.d("MainViewModel Init")
        fetchWallpapers()
    }

    private fun fetchWallpapers() {
        viewModelScope.launch {
            wallpapers.postValue(Resource.loading(null))
            if (networkUtils.isNetworkConnected()) {
                mainRepository.fetchWallpapers(1).let {
                    if (it.isSuccessful) {
                        wallpapers.postValue(Resource.success(it.body()?.data))
                    } else {
                        wallpapers.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } else {
                wallpapers.postValue(Resource.error("No internet connection", null))
            }
        }
    }
}