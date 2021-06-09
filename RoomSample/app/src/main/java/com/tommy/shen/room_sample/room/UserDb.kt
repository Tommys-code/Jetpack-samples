package com.tommy.shen.room_sample.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tommy.shen.room_sample.sqlite.DatabaseHelper


@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserDb : RoomDatabase() {

    companion object {
        private var instance: UserDb? = null

        @Synchronized
        fun get(context: Context): UserDb {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        UserDb::class.java,
                        DatabaseHelper.DBNAME
                    ).allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .build()
            }



            return instance!!
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //表结构没有变，空实现


                //  创建新的临时表
                database.execSQL("CREATE TABLE users_new (id INTEGER NOT NUll, name TEXT NOT NUll, PRIMARY KEY(id))")
                // 复制数据
                database.execSQL("INSERT INTO users_new (id, name) SELECT id, name FROM ${DatabaseHelper.TABLE_NAME}")
                // 删除表结构
                database.execSQL("DROP TABLE ${DatabaseHelper.TABLE_NAME}")
                // 临时表名称更改
                database.execSQL("ALTER TABLE users_new RENAME TO ${DatabaseHelper.TABLE_NAME}")
            }
        }



    }


    abstract fun userDao(): UserDao
}