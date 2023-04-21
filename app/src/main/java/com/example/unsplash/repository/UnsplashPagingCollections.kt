package com.example.unsplash.repository

import android.content.Context
import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.R
import com.example.unsplash.api.UnsplashApi
import com.example.unsplash.dao.CollectionsDao
import com.example.unsplash.data.collections.*
import com.example.unsplash.utils.Constants.UNSPLASH_PLUS
import com.example.unsplash.utils.Constants.UNSPLASH_STARTING_PAGE_INDEX


class UnsplashPagingCollections(
    private val unsplashApi: UnsplashApi,
    private val token: String,
    private val collectionsDao: CollectionsDao,
    private val context: Context
) : PagingSource<Int, Collections>() {
    override fun getRefreshKey(state: PagingState<Int, Collections>)=null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Collections> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        var res: LoadResult<Int, Collections>
        try {
            val response = unsplashApi.getCollections("Bearer $token", position, params.loadSize)

            response.forEach {
                val collection = com.example.unsplash.data.database.Collections(
                    id = it.id!!,
                    title = it.title ?: "",
                    totalPhotos = it.totalPhotos ?: 0,
                    name = it.user!!.name ?: "",
                    userName = it.user!!.username!!,
                    description = it.description ?: "",
                    urlsRegular = it.coverPhoto!!.urls!!.regular!!,
                    urlsFull = it.coverPhoto!!.urls!!.full!!,
                    coverPhotoLikes = it.coverPhoto!!.likes!!,
                    coverPhotoLikedByUser = it.coverPhoto!!.likedByUser!!,
                    profileImageMedium = it.user!!.profileImage!!.medium.toString()
                )
                collectionsDao.insertCollections(collection)
            }
            val responseUnderPlus = response.filter { it.user!!.username!=UNSPLASH_PLUS }
            res = LoadResult.Page(
                data = responseUnderPlus,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            val collectionsFromBD = collectionsDao.getCollections()
            val collections = mutableListOf<Collections>()
            collectionsFromBD.forEach {
                collections.add(
                    Collections(
                        id = it.id,
                        title = it.title,
                        totalPhotos = it.totalPhotos,
                        user = User(
                            username = it.userName,
                            name = it.name,
                            profileImage = ProfileImage(medium = it.profileImageMedium)
                        ),
                        description = it.description,
                        coverPhoto = CoverPhoto(
                            urls = Urls(
                                regular = it.urlsRegular,
                                full = it.urlsFull
                            ),
                            likes = it.coverPhotoLikes,
                            likedByUser = it.coverPhotoLikedByUser
                        ),
                    )
                )
            }
            Toast.makeText(context, R.string.cash, Toast.LENGTH_LONG).show()
            res = LoadResult.Page(data = collections.filter { it.user!!.username!=UNSPLASH_PLUS }, null, null)
            if (collectionsFromBD.isEmpty()) res = LoadResult.Error(e)
        }
        return res
    }
}