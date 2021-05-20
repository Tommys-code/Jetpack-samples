package com.tommy.shen.paging3sample.ui.withother

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class OtherViewModel : ViewModel() {

    val data = Pager(
        PagingConfig(
            // 每页显示的数据的大小
            pageSize = 20
        )
    ) {
        OtherPagingSource()
    }
        .flow
        //cachedIn 绑定协程生命周期
        .cachedIn(viewModelScope)
        //转换成liveData
        //.asLiveData(viewModelScope.coroutineContext)

}