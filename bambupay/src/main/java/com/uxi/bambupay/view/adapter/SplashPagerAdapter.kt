package com.uxi.bambupay.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.uxi.bambupay.R
import com.uxi.bambupay.model.Splash

/**
 * Created by Eraño Payawal on 2019-10-21.
 * hunterxer31@gmail.com
 */
class SplashPagerAdapter(private val context: Context, private var listResources: ArrayList<Splash>?) : PagerAdapter() {

    override fun getCount(): Int {
        return if (listResources != null) {
            listResources!!.size
        } else 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.pager_item, container, false)
        val imageView = itemView.findViewById<View>(R.id.image_pager_item) as ImageView
        val textViewTitle = itemView.findViewById<View>(R.id.txt_title) as TextView
        val textViewSubTitle = itemView.findViewById<View>(R.id.txt_sub_title) as TextView
        if (listResources != null) {

            val item: Splash? = listResources!![position]

            if (imageView != null) {
                val imageResource = item?.image
                imageResource?.let {
                    imageView.setImageResource(it)
                }
            }

            if (textViewTitle != null) {
                textViewTitle.text = item?.title

            }

            if (textViewSubTitle != null) {
                textViewSubTitle.text = item?.subTitle
            }

        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}