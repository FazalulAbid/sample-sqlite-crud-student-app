package com.example.week5studentsapp.HelperClasses.General

import android.content.Context

class PublicConstants {
    companion object{
        const val DATABASE_VERSION:Int = 1
        const val DATABASE_NAME: String = "DB.db"
        lateinit var appContext: Context

        // Form submit modes.
        const val FORM_SUBMIT_MODE: String = "FormSubmitMode"
        const val FORM_SUBMIT_MODE_ADD: Int = 1
        const val FORM_SUBMIT_MODE_EDIT: Int = 2

        const val STUDENT_ID: String = "StudentId"
    }
}