package com.test.zomatoapp.models.restaurant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    var address: String,
    var locality: String,
    var city: String
) : Parcelable