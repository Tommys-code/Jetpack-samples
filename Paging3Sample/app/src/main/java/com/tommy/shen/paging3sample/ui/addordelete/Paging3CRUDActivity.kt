package com.tommy.shen.paging3sample.ui.addordelete

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kennyc.view.MultiStateView
import com.tommy.shen.paging3sample.R
import com.tommy.shen.paging3sample.adater.LoadMoreAdapter
import com.tommy.shen.paging3sample.adater.PersonAdapter
import com.tommy.shen.paging3sample.databinding.ActivityOtherBinding
import com.tommy.shen.paging3sample.ui.withother.OtherRepository
import kotlinx.coroutines.flow.collect

class Paging3CRUDActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherBinding

    private val viewModel by viewModels<CRUDViewModel>()
    private val adapter by lazy { PersonAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other)
        binding.lifecycleOwner = this

        binding.recycle.adapter = adapter.withLoadStateFooter(LoadMoreAdapter { adapter.retry() })
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { adapter.submitData(it) }
        }

        binding.refresh.setOnRefreshListener { adapter.refresh() }
        adapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Error -> {
                    binding.refresh.isRefreshing = false
                    //加载出错，显示错误页面
                    binding.multiStateView.viewState = MultiStateView.ViewState.ERROR
                }
                is LoadState.NotLoading -> {
                    binding.refresh.isRefreshing = false
                    //无数据，显示空页面
                    if (it.append is LoadState.NotLoading && it.append.endOfPaginationReached && adapter.itemCount == 0) {
                        binding.multiStateView.viewState = MultiStateView.ViewState.EMPTY
                    } else if (adapter.itemCount > 0) {
                        binding.multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    }
                }
            }
        }
        binding.multiStateView.getView(MultiStateView.ViewState.ERROR)?.setOnClickListener {
            binding.multiStateView.viewState = MultiStateView.ViewState.LOADING
            OtherRepository.type = OtherRepository.COMMON_TYPE
            adapter.retry()
        }
        initSwipeToDelete()
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int =
                makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.remove(adapter.snapshot().toMutableList().apply {
                    removeAt(viewHolder.layoutPosition)
                }.filterNotNull())
                adapter.refresh()
                //假删除
//                adapter.removeItem(viewHolder.layoutPosition)
            }
        }).attachToRecyclerView(binding.recycle)
    }

}