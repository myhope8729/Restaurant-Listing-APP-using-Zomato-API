package com.test.zomatoapp.models.restaurant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchData(
    var results_found: String,
    var results_start: String,
    var results_shown: String,
    var restaurants: MutableList<RestaurantItem> = ArrayList()
) : Parcelable