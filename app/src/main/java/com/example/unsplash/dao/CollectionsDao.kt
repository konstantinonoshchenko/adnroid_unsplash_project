package com.example.unsplash.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.unsplash.data.database.Collections

@Dao
interface CollectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: Collections)

    @Query("SELECT * FROM `collections`")
    suspend fun getCollections():List<Collections>

    @Delete
    suspend fun deleteCollection(collections: Collections)

    @Query("DELETE FROM `collections`")
    suspend fun allDeleteCollections()

}