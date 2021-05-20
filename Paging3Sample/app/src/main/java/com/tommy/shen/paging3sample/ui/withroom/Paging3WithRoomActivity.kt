package com.tommy.shen.paging3sample.ui.withroom

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tommy.shen.paging3sample.R
import com.tommy.shen.paging3sample.adater.PersonAdapter
import com.tommy.shen.paging3sample.databinding.ActivityPaging3WithRoomBinding
import com.tommy.shen.paging3sample.local.PersonDb
import kotlinx.coroutines.flow.collect

class Paging3WithRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaging3WithRoomBinding
    private val viewModel by viewModels<RoomViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                    val personDao =
                        PersonDb.get(this@Paging3WithRoomActivity.applicationContext).personDao()
                    return RoomViewModel(personDao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    private val adapter by lazy { PersonAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_paging3_with_room
        )
        binding.lifecycleOwner = this

        binding.recycle.adapter = adapter
        lifecycleScope.launchWhenCreated {
            viewModel.data.collect { adapter.submitData(it) }
        }
        initSwipeToDelete()
    }

    /**
     * 调用 ItemTouchHelper 实现 左右滑动 删除 item 功能
     */
    private fun initSwipeToDelete() {

        /**
         * 位于 [androidx.recyclerview.widget] 包下，已经封装好的控件
         */
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
                adapter.peek(viewHolder.layoutPosition)?.let { viewModel.remove(it) }
            }
        }).attachToRecyclerView(binding.recycle)
    }

}