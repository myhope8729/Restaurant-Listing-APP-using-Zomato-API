package com.test.zomatoapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NavUtils
import androidx.appcompat.app.ActionBar
import android.view.MenuItem
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

import com.test.zomatoapp.dummy.DummyContent
import com.test.zomatoapp.interceptor.AuthInterceptor
import com.test.zomatoapp.models.category.CategoryData
import com.test.zomatoapp.models.restaurant.Restaurant
import com.test.zomatoapp.models.restaurant.RestaurantItem
import com.test.zomatoapp.models.restaurant.SearchData
import com.test.zomatoapp.services.ZomatoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import kotlinx.android.synthetic.main.restaurant_list_content.view.*
import kotlinx.android.synthetic.main.restaurant_list.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [RestaurantDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class RestaurantListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var myCompositeDisposable: CompositeDisposable? = CompositeDisposable()
    private val BASE_URL = "https://developers.zomato.com/api/v2.1/"
    private var twoPane: Boolean = false
    private var searchData: SearchData? = null
    var adapter: RestaurantGridAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        val category_id = intent.getStringExtra(ARG_CATEGORY_ID)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(AuthInterceptor())

        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ZomatoService::class.java)

        myCompositeDisposable?.add(requestInterface.searchRestaurants(category_id,0, 20)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse))

    }

    private fun handleResponse(data: SearchData) {

        setSupportActionBar(toolbar)
        toolbar.title = title

        searchData = data

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = RestaurantGridAdapter(this, searchData?.restaurants)
        restaurant_list.adapter = adapter
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
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    class RestaurantGridAdapter : BaseAdapter {
        private var restaurantList: MutableList<RestaurantItem>? = null
        var context: Context? = null
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Restaurant
                val intent = Intent(v.context, RestaurantDetailActivity::class.java).apply {
                    putExtra(RestaurantDetailFragment.ARG_ITEM_ID, item.id)
                }
                v.context.startActivity(intent)
            }
        }
        constructor(context: Context, restaurantList: MutableList<RestaurantItem>?) :super() {
            this.restaurantList = restaurantList
            this.context = context
        }
        override fun getCount(): Int {
            return restaurantList?.size?:0
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): RestaurantItem? {
            return restaurantList?.get(position)
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val restaurantItem = this.restaurantList?.get(p0)

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var restaurantView = inflator.inflate(R.layout.restaurant_list_content, null)

            Picasso.with(context).load(restaurantItem?.restaurant?.thumb).into(restaurantView.restaurant_image)
            restaurantView.restaurant_name.text = restaurantItem?.restaurant?.name
            restaurantView.restaurant_address.text = restaurantItem?.restaurant?.location?.address

            with(restaurantView){
                tag = restaurantItem?.restaurant
                setOnClickListener(onClickListener)
            }

            return restaurantView
        }
    }

    /*class SimpleItemRecyclerViewAdapter(
        private val parentActivity: RestaurantListActivity,
        private val values: MutableList<RestaurantItem>?,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Restaurant
                if (twoPane) {
                    val fragment = RestaurantDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(RestaurantDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.restaurant_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, RestaurantDetailActivity::class.java).apply {
                        putExtra(RestaurantDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.restaurant_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values?.get(position)

            holder.restaurantImage.setImageURI(Uri.parse(item?.restaurant?.thumb))
            holder.restaurantName.text = item?.restaurant?.name

            with(holder.itemView) {
                tag = item?.restaurant
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values?.size?:0

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val restaurantImage: ImageView = view.restaurant_image
            val restaurantName: TextView = view.restaurant_name
        }
    }
*/
    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_CATEGORY_ID = "category_id"
    }
}
