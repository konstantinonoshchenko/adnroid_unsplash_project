package com.example.unsplash.dao

import androidx.room.*
import com.example.unsplash.data.database.CollectionsPhotos

@Dao
interface CollectionsPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollectionsPhotos(collectionsPhotos: CollectionsPhotos)

    @Query("SELECT * FROM `collectionsPhoto` WHERE idCollection = :idCollection")
    suspend fun getCollectionsPhotos(idCollection:String):List<CollectionsPhotos>

    @Delete
    suspend fun deleteCollectionsPhotos(collectionsPhotos: CollectionsPhotos)

    @Query("DELETE FROM `collectionsPhoto`")
    suspend fun allDeleteCollectionsPhotos()
}