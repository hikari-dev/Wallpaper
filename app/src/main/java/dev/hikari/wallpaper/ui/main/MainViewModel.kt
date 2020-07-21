package dev.hikari.wallpaper.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hikari.wallpaper.model.Wallpaper
import dev.hikari.wallpaper.repository.MainRepository
import dev.hikari.wallpaper.utils.NetworkUtils
import dev.hikari.wallpaper.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    val wallpapers = MutableLiveData<Resource<List<Wallpaper>>>()

    init {
        Timber.d("MainViewModel Init")
        fetchWallpapers(1)
    }

    fun fetchWallpapers(page: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
            wallpapers.postValue(Resource.error(throwable.message ?: "unknown error", null))
        }) {
            wallpapers.postValue(Resource.loading(null))
            if (networkUtils.isNetworkConnected()) {
                mainRepository.fetchWallpapers(page).let {
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