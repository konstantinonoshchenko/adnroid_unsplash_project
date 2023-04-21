package com.example.unsplash.data.collections

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Subcategory (

  @SerializedName("slug"        ) var slug       : String? = null,
  @SerializedName("pretty_slug" ) var prettySlug : String? = null

): Parcelable