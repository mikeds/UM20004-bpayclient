package com.uxi.bambupay.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.uxi.bambupay.databinding.CustomSpinnerItemBinding
import com.uxi.bambupay.model.registration.Fund


/**
 * Created by Eraño Payawal on 4/8/21.
 * hunterxer31@gmail.com
 */
class FundsAdapter(private val lists: List<Fund>) : BaseAdapter() {

    override fun getCount(): Int = lists.size

    override fun getItem(position: Int): Fund = lists[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = CustomSpinnerItemBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        val view = convertView ?: binding.root

        val vh = ItemHolder(binding)
        view.tag = vh

        vh.label.text = getItem(position).name

        return view
    }

    private class ItemHolder(row: CustomSpinnerItemBinding) {
        val label: TextView = row.text
    }
}