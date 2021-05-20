package com.tommy.shen.paging3sample.ui.addordelete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.tommy.shen.paging3sample.ui.withother.OtherRepository
import com.tommy.shen.paging3sample.local.PersonData

class CRUDViewModel : ViewModel() {

    val data = Pager(
        PagingConfig(
            // 每页显示的数据的大小
            pageSize = 20
        )
    ) {
        object : PagingSource<Int, PersonData>() {

            override fun getRefreshKey(state: PagingState<Int, PersonData>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonData> {
                return try {
                    //页码未定义置为0
                    val currentPage = params.key ?: 0
                    //仓库层请求数据
                    val demoReqData = getData(currentPage)
                    //当前页码 小于 总页码 页面加1
                    val nextPage = if (!demoReqData.isNullOrEmpty()) {
                        pageNum + 1
                    } else {
                        //没有更多数据
                        null
                    }
                    if (demoReqData != null) {
                        LoadResult.Page(data = demoReqData, prevKey = null, nextKey = nextPage)
                    } else {
                        LoadResult.Error(throwable = Throwable())
                    }
                } catch (e: Exception) {
                    LoadResult.Error(throwable = e)
                }
            }

        }
    }.flow.cachedIn(viewModelScope)

    private var loadCache = false
    private var newData: List<PersonData>? = null
    private var pageNum = 0

    private suspend fun getData(page: Int): List<PersonData>? {
        return if (loadCache) {
            loadCache = false
            newData
        } else {
            OtherRepository.loadData(page).apply {
                //数据加载完后才能赋值当前页数
                pageNum = page
            }
        }
    }

    fun remove(data: List<PersonData>) {
        loadCache = true
        newData = data
    }

}