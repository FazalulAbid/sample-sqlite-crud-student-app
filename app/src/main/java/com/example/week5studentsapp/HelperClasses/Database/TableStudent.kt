package com.example.week5studentsapp.HelperClasses.Database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.week5studentsapp.HelperClasses.DataClasses.Student
import com.example.week5studentsapp.HelperClasses.General.PublicConstants

class TableStudent {
    companion object {
        const val TABLE: String = "tbl_Student"

        // Student table columns
        const val COL_ID = "Id"
        const val COL_FIRST_NAME = "FirstName"
        const val COL_LAST_NAME = "LastName"
        const val COL_EMAIL = "Email"
        const val COL_DEPARTMENT = "Department"
        const val COL_IMAGE = "Image"

        // Create table query for student table.
        const val STUDENT_TABLE_QUERY: String =
            ("CREATE TABLE $TABLE ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_FIRST_NAME TEXT NOT NULL,$COL_LAST_NAME TEXT NOT NULL,$COL_EMAIL TEXT NOT NULL, $COL_DEPARTMENT TEXT, $COL_IMAGE BLOB)")
    }

    fun saveStudent(
        student: Student, submitMode: Int = PublicConstants.FORM_SUBMIT_MODE_ADD
    ): Boolean {
        val writableDatabase: SQLiteDatabase = DBConnection.WRITABLE_DB

        // Set all values form student data class to content values.
        val values: ContentValues = ContentValues().apply {
            this.put(COL_FIRST_NAME, student.firstName)
            this.put(COL_LAST_NAME, student.lastName)
            this.put(COL_EMAIL, student.email)
            this.put(COL_DEPARTMENT, student.department)
            this.put(COL_IMAGE, student.image)
        }

        val result: Long = if (submitMode == PublicConstants.FORM_SUBMIT_MODE_ADD) {
            //  Insert into table student
            writableDatabase.insert(TABLE, null, values)
        } else {
            writableDatabase.update(TABLE, values, "$COL_ID=?", arrayOf("${student.id}")).toLong()
        }

        // Check if the query executed or not.
        if (result.toInt() == -1) return false
        return true
    }

    fun viewAllStudents(): Cursor? {
        // Select query for view all students.
        val selectStudentQuery =
            "SELECT * FROM $TABLE"

        // Initialize readable database with instance of readable database from database connection.
        val readableDatabase = DBConnection.READABLE_DB

        // Get student data and return as cursor.
        return readableDatabase.rawQuery(selectStudentQuery, null)
    }

    fun viewStudent(studentId: Int): Student {
        // Declare and initialize readable database.
        val readableDatabase: SQLiteDatabase = DBConnection.READABLE_DB

        // Read student details with student id from database.
        val cursor = readableDatabase.query(
            TABLE, arrayOf(
                COL_ID,
                COL_FIRST_NAME,
                COL_LAST_NAME,
                COL_EMAIL,
                COL_DEPARTMENT,
                COL_IMAGE
            ), "$COL_ID=?", arrayOf("$studentId"), null, null, null, null
        )

        // Return Student object after setting data from cursor.
        cursor?.moveToFirst()
        return Student(
            cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(COL_DEPARTMENT)),
            cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE))
        )
    }

    fun deleteStudent(studentId: Int): Boolean {
        // Declare and initialize writable database.
        val writableDatabase: SQLiteDatabase = DBConnection.WRITABLE_DB

        // Delete student with student id.
        val result = writableDatabase.delete(TABLE, "$COL_ID=?", arrayOf("$studentId"))

        // Check if the query executed or not.
        if (result.toInt() == -1) return false
        return true
    }
}