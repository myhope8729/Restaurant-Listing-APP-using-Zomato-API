package com.test.zomatoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.test.zomatoapp.dummy.DummyContent
import com.test.zomatoapp.models.category.Category
import com.test.zomatoapp.models.category.CategoryData
import com.test.zomatoapp.models.category.CategoryItem
import kotlinx.android.synthetic.main.activity_category_list.*
import kotlinx.android.synthetic.main.category_list_content.view.*
import kotlinx.android.synthetic.main.category_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [RestrauntListActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class CategoryListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private var categoryData: CategoryData? = null
    val PAGE_TITLE: String = "Categories"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        val data = intent.extras
        categoryData = data?.getParcelable<CategoryData>("data")

        setSupportActionBar(toolbar)
        toolbar.title = PAGE_TITLE

        /*if (item_detail_container != null) {

            twoPane = true
        }*/

        setupCategoryView(category_list)
    }

    private fun setupCategoryView(categoryView: RecyclerView) {
        categoryView.adapter = SimpleCategoryViewAdapter(this, categoryData?.categories, twoPane)
    }

    class SimpleCategoryViewAdapter(
        private val parentActivity: CategoryListActivity,
        private val values: MutableList<CategoryItem>?,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleCategoryViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val category = v.tag as Category
/*                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {*/
                val intent = Intent(v.context, RestaurantListActivity::class.java).apply {
                    putExtra(RestaurantListActivity.ARG_CATEGORY_ID, category.id)
                }
                v.context.startActivity(intent)
                //}
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val category = values?.get(position)
            holder.idView.text = category?.categories?.id
            holder.contentView.text = category?.categories?.name

            with(holder.itemView) {
                tag = category?.categories
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() : Int = values?.size?:0

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
