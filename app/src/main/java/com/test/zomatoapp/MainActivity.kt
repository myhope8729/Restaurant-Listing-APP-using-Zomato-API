package com.test.zomatoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.test.zomatoapp.interceptor.AuthInterceptor
import com.test.zomatoapp.services.ZomatoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import com.test.zomatoapp.models.category.CategoryData

class MainActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = null
    private val BASE_URL = "https://developers.zomato.com/api/v2.1/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myCompositeDisposable = CompositeDisposable()
        loadCategories()
    }

    private fun loadCategories() {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(AuthInterceptor())

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ZomatoService::class.java)

        myCompositeDisposable?.add(requestInterface.getCategories()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(data: CategoryData) {

        val intent = Intent(this, CategoryListActivity::class.java)
        intent.putExtra("data", data)
        //startActivity(intent)

        startActivity(intent)
        finish()
    }
}
