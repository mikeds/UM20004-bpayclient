package com.uxi.bambupay.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.uxi.bambupay.model.registration.Work

/**
 * Created by Eraño Payawal on 4/8/21.
 * hunterxer31@gmail.com
 */
object WorkItemDiff : DiffUtil.ItemCallback<Work>() {

    override fun areItemsTheSame(oldItem: Work, newItem: Work): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Work, newItem: Work): Boolean = oldItem == newItem
}