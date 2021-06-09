package com.tommy.shen.room_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.tommy.shen.room_sample.room.UserDb
import com.tommy.shen.room_sample.sqlite.DatabaseHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.concurrent.Executors

//数据库迁移
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val dao by lazy { UserDb.get(this).userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.movementMethod = ScrollingMovementMethod.getInstance()

        DatabaseHelper.init(this)

        findViewById<View>(R.id.btn0).setOnClickListener(this)
        findViewById<View>(R.id.btn1).setOnClickListener(this)
        findViewById<View>(R.id.btn2).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn0 -> getData {
                try {
                    DatabaseHelper.getUsers().toString()
                } catch (e: Exception) {
                    "数据库已迁移"
                }
            }
            R.id.btn1 -> getData {
                dao
                ""
            }
            R.id.btn2 -> getData { dao.getAllNames().toString() }
        }
    }

    //room增删改查不能在主线程执行
    private fun getData(function: () -> String) {
        Executors.newSingleThreadExecutor().execute {
            val data = function.invoke()
            runOnUiThread {
                tv.text = data
            }
        }
    }
}