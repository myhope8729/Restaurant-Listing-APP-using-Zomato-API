package com.test.zomatoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.zomatoapp.dummy.DummyContent
import com.test.zomatoapp.models.restaurant.Restaurant
import kotlinx.android.synthetic.main.activity_restaurant_detail.*
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import kotlinx.android.synthetic.main.restaurant_detail.view.*
import kotlinx.android.synthetic.main.restaurant_list_content.view.*
import org.parceler.Parcels

/**
 * A fragment representing a single Restaurant detail screen.
 * This fragment is either contained in a [RestaurantListActivity]
 * in two-pane mode (on tablets) or a [RestaurantDetailActivity]
 * on handsets.
 */
class RestaurantDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var restaurant: Restaurant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            restaurant = Parcels.unwrap(it.getParcelable("data"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.restaurant_detail, container, false)

        Picasso.with(context).load(restaurant?.featured_image).into(rootView.restaurant_detail_image)
        rootView.restaurant_detail_name.text = restaurant?.name
        rootView.restaurant_detail_location.text = "Address:" + restaurant?.location?.address
        rootView.restaurant_detail_phone.text = "Phone:" + restaurant?.phone_numbers

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
