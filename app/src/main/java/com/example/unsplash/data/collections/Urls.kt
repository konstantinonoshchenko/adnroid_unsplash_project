package com.example.unsplash.data.collections

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Urls (

  @SerializedName("raw"      ) var raw     : String? = null,
  @SerializedName("full"     ) var full    : String? = null,
  @SerializedName("regular"  ) var regular : String? = null,
  @SerializedName("small"    ) var small   : String? = null,
  @SerializedName("thumb"    ) var thumb   : String? = null,
  @SerializedName("small_s3" ) var smallS3 : String? = null

): Parcelable