package com.test.zomatoapp.models.category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryItem(
    var categories: Category
) : Parcelable