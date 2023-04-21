package com.example.unsplash.data

import com.google.gson.annotations.SerializedName


data class UnsplashSearchPhotoResponse(

    @SerializedName("total") var total: Int? = null,
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("results") var results: List<Results> = listOf(),

    )