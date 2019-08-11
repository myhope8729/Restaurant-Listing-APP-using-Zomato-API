package com.test.zomatoapp.models.restaurant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurant(
    var id: String,
    var name: String,
    var url: String,
    var location: Location,
    var average_cost_for_two: String,
    var currency: String,
    var thumb: String,
    var featured_image: String,
    var phone_numbers: String
) : Parcelable