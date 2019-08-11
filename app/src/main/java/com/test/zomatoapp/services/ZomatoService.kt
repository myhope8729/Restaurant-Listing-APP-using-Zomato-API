package com.test.zomatoapp.services

import com.test.zomatoapp.models.category.CategoryData
import com.test.zomatoapp.models.restaurant.Restaurant
import com.test.zomatoapp.models.restaurant.SearchData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ZomatoService {
    @GET("categories")
    fun getCategories() : Observable<CategoryData>

    @GET("search")
    fun searchRestaurants(@Query("category") category: String?, @Query("start") start: Int, @Query("count") count: Int) : Observable<SearchData>

    @GET("restaurant")
    fun getRestaurant(@Query("res_id") res_id: String?) : Observable<Restaurant>
}