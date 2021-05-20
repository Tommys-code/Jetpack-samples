package com.tommy.shen.paging3sample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tommy.shen.paging3sample.ui.addordelete.Paging3CRUDActivity
import com.tommy.shen.paging3sample.ui.withother.OtherPagingActivity
import com.tommy.shen.paging3sample.ui.withother.OtherRepository
import com.tommy.shen.paging3sample.ui.withroom.Paging3WithRoomActivity
import com.tommy.shen.paging3sample.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn1).setOnClickListener {
            startActivity(Intent(this, Paging3WithRoomActivity::class.java))
        }
        findViewById<Button>(R.id.btn2).setOnClickListener {
            startActivity(Intent(this, OtherPagingActivity::class.java).apply {
                putExtra("type", OtherRepository.FIRST_ERROR_TYPE)
            })
        }
        findViewById<Button>(R.id.btn3).setOnClickListener {
            startActivity(Intent(this, OtherPagingActivity::class.java).apply {
                putExtra("type", OtherRepository.EMPTY_TYPE)
            })
        }
        findViewById<Button>(R.id.btn4).setOnClickListener {
            startActivity(Intent(this, OtherPagingActivity::class.java).apply {
                putExtra("type", OtherRepository.LOAD_MORE_ERROR_TYPE)
            })
        }
        findViewById<Button>(R.id.btn5).setOnClickListener {
            startActivity(Intent(this, OtherPagingActivity::class.java).apply {
                putExtra("type", OtherRepository.COMMON_TYPE)
            })
        }
        findViewById<Button>(R.id.btn6).setOnClickListener {
            startActivity(Intent(this, Paging3CRUDActivity::class.java))
        }
    }
}