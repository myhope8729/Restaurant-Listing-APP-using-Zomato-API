package com.test.zomatoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import android.view.MenuItem
import com.test.zomatoapp.interceptor.AuthInterceptor
import com.test.zomatoapp.models.restaurant.Restaurant
import com.test.zomatoapp.services.ZomatoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurant_detail.*
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import org.parceler.Parcels;

/**
 * An activity representing a single Restaurant detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [RestaurantListActivity].
 */
class RestaurantDetailActivity : AppCompatActivity() {

    private var myCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    private val BASE_URL = "https://developers.zomato.com/api/v2.1/"
    private var savedState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        savedState = savedInstanceState

        val restaurant_id = intent.getStringExtra(RestaurantDetailFragment.ARG_ITEM_ID)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(AuthInterceptor())

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ZomatoService::class.java)

        myCompositeDisposable?.add(requestInterface.getRestaurant(restaurant_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(data: Restaurant){
        setSupportActionBar(detail_toolbar)
        detail_toolbar.title = data.name

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = RestaurantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data", Parcels.wrap(data));
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.restaurant_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                NavUtils.navigateUpTo(this, Intent(this, RestaurantListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
