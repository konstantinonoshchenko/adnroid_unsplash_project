package com.example.unsplash.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Position (

  @SerializedName("latitude"  ) var latitude  : String? = null,
  @SerializedName("longitude" ) var longitude : String? = null

): Parcelable