package com.tommy.shen.paging3sample.adater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tommy.shen.paging3sample.R
import com.tommy.shen.paging3sample.databinding.ItemPersonBinding
import com.tommy.shen.paging3sample.local.PersonData

class PersonAdapter : PagingDataAdapter<PersonData, RecyclerCompatVH<ViewDataBinding>>(object :
    DiffUtil.ItemCallback<PersonData>() {
    override fun areItemsTheSame(oldItem: PersonData, newItem: PersonData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PersonData, newItem: PersonData): Boolean =
        oldItem == newItem

}) {

    override fun onBindViewHolder(holder: RecyclerCompatVH<ViewDataBinding>, position: Int) {
        getItem(position)?.run {
            when (holder.binding) {
                is ItemPersonBinding -> holder.binding.name = name
            }
        }
    }

    fun removeItem(position: Int) {
        getItem(position)?.let { it.delete = true }
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerCompatVH<ViewDataBinding> {
        return RecyclerCompatVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                if (viewType == 1) R.layout.item_delete else R.layout.item_person,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.delete == true) 1 else 0
    }

}