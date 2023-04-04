package com.example.week5studentsapp.HelperClasses.DataClasses

data class Student(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val department: String?,
    val image: ByteArray?
)