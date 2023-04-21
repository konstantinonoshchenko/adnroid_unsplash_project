package com.example.unsplash.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TexturesPatterns (

  @SerializedName("status"      ) var status     : String? = null,
  @SerializedName("approved_on" ) var approvedOn : String? = null

): Parcelable