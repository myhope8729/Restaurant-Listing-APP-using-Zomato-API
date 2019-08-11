package com.test.zomatoapp.models.category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryData(
    var categories: MutableList<CategoryItem> = ArrayList()
) : Parcelable