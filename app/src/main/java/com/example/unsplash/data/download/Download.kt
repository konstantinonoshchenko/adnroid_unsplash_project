package com.example.unsplash.data.download

import com.example.unsplash.data.Type
import com.google.gson.annotations.SerializedName

data class Download(
    @SerializedName("url") var url: String? = null,
)
