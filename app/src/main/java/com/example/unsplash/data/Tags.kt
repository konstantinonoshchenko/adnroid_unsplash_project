package com.example.unsplash.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tags (

  @SerializedName("type"  ) var type  : String? = null,
  @SerializedName("title" ) var title : String? = null

): Parcelable