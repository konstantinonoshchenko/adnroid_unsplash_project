package com.example.unsplash.data.collections

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopicSubmissions (

  @SerializedName("animals" ) var animals : Animals? = Animals()

): Parcelable