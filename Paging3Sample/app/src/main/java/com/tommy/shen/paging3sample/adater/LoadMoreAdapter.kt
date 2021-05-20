package com.tommy.shen.paging3sample.adater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.tommy.shen.paging3sample.R
import com.tommy.shen.paging3sample.databinding.ItemLoadMoreBinding

class LoadMoreAdapter(private val retryCallBack: () -> Unit) :
    LoadStateAdapter<RecyclerCompatVH<ItemLoadMoreBinding>>() {

    override fun onBindViewHolder(
        holder: RecyclerCompatVH<ItemLoadMoreBinding>,
        loadState: LoadState
    ) {
        //没有下一页
        holder.binding.noData.visibility =
            if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached) View.VISIBLE else View.GONE
        //加载中
        holder.binding.loding.visibility =
            if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
        //出错，重试按钮
        holder.binding.btnRetry.visibility =
            if (loadState is LoadState.Error) View.VISIBLE else View.GONE

        holder.binding.btnRetry.setOnClickListener {
            retryCallBack.invoke()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerCompatVH<ItemLoadMoreBinding> {
        return RecyclerCompatVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_load_more, parent, false
            )
        )
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState) || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }

}