package com.example.unsplash.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links (

  @SerializedName("self"      ) var self      : String? = null,
  @SerializedName("html"      ) var html      : String? = null,
  @SerializedName("photos"    ) var photos    : String? = null,
  @SerializedName("likes"     ) var likes     : String? = null,
  @SerializedName("portfolio" ) var portfolio : String? = null,
  @SerializedName("following" ) var following : String? = null,
  @SerializedName("followers" ) var followers : String? = null

): Parcelable