package com.example.unsplash.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.dao.*
import com.example.unsplash.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi =
        retrofit.create(UnsplashApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app:Application) = Room.databaseBuilder(
        app,
        UnsplashDatabase::class.java,
        "UnsplashDB"
    ).build()

    @Provides
    @Singleton
    fun getCollectionsDao(appDB:UnsplashDatabase): CollectionsDao = appDB.getCollectionsDao()

    @Provides
    @Singleton
    fun getMyCollectionsDao(appDB:UnsplashDatabase): MyCollectionsDao = appDB.getMyCollectionsDao()

    @Provides
    @Singleton
    fun getMyPhotosDao(appDB:UnsplashDatabase): MyPhotosDao = appDB.getMyPhotosDao()

    @Provides
    @Singleton
    fun getPhotosDao(appDB:UnsplashDatabase): PhotosDao = appDB.getPhotosDao()

    @Provides
    @Singleton
    fun getPhotosLikesMeDao(appDB:UnsplashDatabase): PhotosLikesMeDao = appDB.getPhotosLikesMeDao()

    @Provides
    @Singleton
    fun getUserDao(appDB:UnsplashDatabase): UserDao=appDB.getUserDao()

    @Provides
    @Singleton
    fun getUserTopPhotosDao(appDB:UnsplashDatabase): TopPhotosDao=appDB.getTopPhotosDao()

    @Provides
    @Singleton
    fun getCollectionPhotoDao(appDB: UnsplashDatabase):CollectionsPhotoDao=appDB.getCollectionsPhotoDao()

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext
}