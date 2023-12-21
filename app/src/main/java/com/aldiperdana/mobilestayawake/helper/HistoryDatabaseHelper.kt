package com.aldiperdana.mobilestayawake.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aldiperdana.mobilestayawake.data.model.HistoryModel

class HistoryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "history.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allhistory"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_START_TIME = "start_time"
        private const val COLUMN_FINISH_TIME = "finish_time"
        private const val COLUMN_DURATION = "duration"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_START_TIME TEXT, $COLUMN_FINISH_TIME TEXT, $COLUMN_DURATION TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertHistory(history: HistoryModel) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, history.title)
            put(COLUMN_START_TIME, history.start_time)
            put(COLUMN_FINISH_TIME, history.finish_time)
            put(COLUMN_DURATION, history.duration)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllHistory(): List<HistoryModel> {
        val historyList = mutableListOf<HistoryModel>()
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_FINISH_TIME DESC"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val start_time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME))
            val finish_time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FINISH_TIME))
            val duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION))

            val history = HistoryModel(id, title, start_time, finish_time, duration)
            historyList.add(history)
        }

        cursor.close()
        db.close()
        return historyList
    }

}