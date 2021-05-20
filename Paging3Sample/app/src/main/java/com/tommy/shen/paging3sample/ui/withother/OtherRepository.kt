package com.tommy.shen.paging3sample.ui.withother

import android.util.Log
import com.tommy.shen.paging3sample.local.PersonData
import kotlinx.coroutines.delay

object OtherRepository {

    private val list =
        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
    private const val totalPage = 5

    const val COMMON_TYPE = 0
    const val EMPTY_TYPE = 1
    const val FIRST_ERROR_TYPE = 2
    const val LOAD_MORE_ERROR_TYPE = 3

    var type = COMMON_TYPE

    suspend fun loadData(pageNum: Int): List<PersonData>? {
        delay(2000)
        Log.i("qwe", "$pageNum")
        return when (type) {
            EMPTY_TYPE -> getEmptyData()
            FIRST_ERROR_TYPE -> null
            LOAD_MORE_ERROR_TYPE -> if (pageNum == 1) null else list.map {
                PersonData(id = pageNum * 100 + it, name = "name-$pageNum-$it")
            }
            else -> if (pageNum > totalPage) getEmptyData() else list.map {
                PersonData(id = pageNum * 100 + it, name = "name-$pageNum-$it")
            }
        }
    }

    //空数据
    private fun getEmptyData(): List<PersonData> {
        return emptyList()
    }

}