package com.test.zomatoapp.models.restaurant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantItem(
    var restaurant: Restaurant
) : Parcelable