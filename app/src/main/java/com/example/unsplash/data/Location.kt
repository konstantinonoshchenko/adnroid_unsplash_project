package com.example.unsplash.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location (

  @SerializedName("name"     ) var name     : String?   = null,
  @SerializedName("city"     ) var city     : String?   = null,
  @SerializedName("country"  ) var country  : String?   = null,
  @SerializedName("position" ) var position : Position? = Position()

): Parcelable