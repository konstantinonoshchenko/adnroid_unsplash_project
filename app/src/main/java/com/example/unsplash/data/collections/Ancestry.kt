package com.example.unsplash.data.collections

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ancestry (

  @SerializedName("type"     ) var type     : Type?     = Type(),
  @SerializedName("category" ) var category : Category? = Category()

): Parcelable