package com.example.unsplash.dao

import androidx.room.*
import com.example.unsplash.data.database.CollectionsPhotos
import com.example.unsplash.data.database.Photos
import com.example.unsplash.data.database.TopPhotos

@Dao
interface TopPhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopPhoto(photos: TopPhotos)

    @Query("SELECT * FROM `topPhotos` WHERE id = :id")
    suspend fun getTopPhotos(id:String):List<TopPhotos>

    @Delete
    suspend fun deleteTopPhotos(photos: TopPhotos)


}