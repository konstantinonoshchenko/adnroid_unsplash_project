package com.example.unsplash.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelatedCollections (

  @SerializedName("total"   ) var total   : Int?               = null,
  @SerializedName("type"    ) var type    : String?            = null,
  @SerializedName("results" ) var results : ArrayList<Results> = arrayListOf()

): Parcelable