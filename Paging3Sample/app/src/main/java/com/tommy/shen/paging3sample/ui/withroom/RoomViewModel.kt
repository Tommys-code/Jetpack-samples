package com.tommy.shen.paging3sample.ui.withroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.tommy.shen.paging3sample.local.PersonDao
import com.tommy.shen.paging3sample.local.PersonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoomViewModel(private val dao: PersonDao) : ViewModel() {

    val data: Flow<PagingData<PersonData>> = Pager(
        PagingConfig(
            // 每页显示的数据的大小
            pageSize = 60,
            // 开启占位符
            enablePlaceholders = true,
            // 预刷新的距离，距离最后一个 item 多远时加载数据
            prefetchDistance = 3,
            //初始化加载数量，默认为 pageSize * 3
            initialLoadSize = 60,
            //一次应在内存中保存的最大数据,这个数字将会触发，滑动加载更多的数据
            maxSize = 200
        )
    ) {
        dao.allPerson()
    }.flow

    fun remove(data: PersonData) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(data)
        }
    }

}