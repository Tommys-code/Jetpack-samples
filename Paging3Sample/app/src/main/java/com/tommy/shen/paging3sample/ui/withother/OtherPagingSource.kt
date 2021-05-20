package com.tommy.shen.paging3sample.ui.withother

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tommy.shen.paging3sample.local.PersonData

class OtherPagingSource : PagingSource<Int, PersonData>() {

    override fun getRefreshKey(state: PagingState<Int, PersonData>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonData> {
        return try {
            //页码未定义置为0
            val currentPage = params.key ?: 0
            //仓库层请求数据
            val demoReqData = OtherRepository.loadData(currentPage)
            //当前页码 小于 总页码 页面加1
            val nextPage = if (!demoReqData.isNullOrEmpty()) {
                currentPage + 1
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