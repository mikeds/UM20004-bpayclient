package com.uxi.bambupay.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.uxi.bambupay.R
import com.uxi.bambupay.model.Province

/**
 * Created by Eraño Payawal on 9/21/20.
 * hunterxer31@gmail.com
 */
class ProvinceAdapter(val context: Context?, var list: MutableList<Province>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.custom_spinner_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = list[position].provinceName

        return view
    }

    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.text) as TextView
    }

    fun updateAdapter(list: MutableList<Province>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}