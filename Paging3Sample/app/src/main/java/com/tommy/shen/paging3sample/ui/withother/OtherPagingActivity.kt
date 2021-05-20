package com.tommy.shen.paging3sample.ui.withother

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.kennyc.view.MultiStateView
import com.tommy.shen.paging3sample.R
import com.tommy.shen.paging3sample.adater.LoadMoreAdapter
import com.tommy.shen.paging3sample.adater.PersonAdapter
import com.tommy.shen.paging3sample.databinding.ActivityOtherBinding
import kotlinx.coroutines.flow.collect

class OtherPagingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherBinding
    private val viewModel by viewModels<OtherViewModel>()
    private val adapter by lazy { PersonAdapter() }

    private val type by lazy { intent.getIntExtra("type", 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other)
        binding.lifecycleOwner = this

        OtherRepository.type = type

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
    }
}