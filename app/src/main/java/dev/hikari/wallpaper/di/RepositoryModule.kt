package dev.hikari.wallpaper.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.hikari.wallpaper.data.network.WallpaperClient
import dev.hikari.wallpaper.repository.MainRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideMainRepository(wallpaperClient: WallpaperClient): MainRepository =
        MainRepository(wallpaperClient)

}