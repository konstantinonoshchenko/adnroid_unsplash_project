package com.example.unsplash.dao

import androidx.room.*
import com.example.unsplash.data.database.MyPhotos

@Dao
interface MyPhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyPhotos(myPhotos: MyPhotos)

    @Query("SELECT * FROM `myPhotos`")
    suspend fun getMyPhotos():List<MyPhotos>

    @Delete
    suspend fun deleteMyPhotos(myPhotos: MyPhotos)

    @Query("DELETE FROM `myPhotos`")
    suspend fun allDeleteMyPhotos()
}