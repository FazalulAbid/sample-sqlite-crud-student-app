package com.example.week5studentsapp.HelperClasses.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.week5studentsapp.HelperClasses.General.PublicConstants

class DBConnection(context: Context) : SQLiteOpenHelper(context,PublicConstants.DATABASE_NAME,null,PublicConstants.DATABASE_VERSION) {
    companion object{
        lateinit var READABLE_DB: SQLiteDatabase;
        lateinit var WRITABLE_DB: SQLiteDatabase;
        lateinit var DB_TABLES_QUERIES: ArrayList<String>
        lateinit var DB_TABLE_NAMES: ArrayList<String>

        fun loadTableStringsToArrays(){
            // Initialize database tables and table names arrays.
            DB_TABLES_QUERIES = ArrayList()
            DB_TABLE_NAMES = ArrayList()

            // Table Student
            DB_TABLE_NAMES.add(TableStudent.TABLE)
            DB_TABLES_QUERIES.add(TableStudent.STUDENT_TABLE_QUERY)
        }
    }

    init {
        // Create database tables.
        loadTableStringsToArrays()

        // Initialize readable and writable database.
        WRITABLE_DB = this.writableDatabase
        READABLE_DB = this.readableDatabase
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Loop through tables array and execute each create table query.
        for (query in DB_TABLES_QUERIES) db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // Loop through each table name and drop if it exists
        for (table in DB_TABLE_NAMES) db?.execSQL("DROP table IF EXISTS $table")

        // Re create tables
        onCreate(db)
    }
}