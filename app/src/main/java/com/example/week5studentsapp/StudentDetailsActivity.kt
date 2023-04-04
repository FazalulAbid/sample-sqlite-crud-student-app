package com.example.week5studentsapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week5studentsapp.HelperClasses.Database.TableStudent
import com.example.week5studentsapp.HelperClasses.General.PublicConstants

class StudentDetailsActivity : AppCompatActivity() {
    // Declare views.
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewDepartment: TextView
    private lateinit var imageViewStudentImage: ImageView

    // Variables.
    private var studentId: String? = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)
        initializeComponents()
    }

    private fun initializeComponents() {
        //View Hooks.
        textViewName = findViewById(R.id.tv_student_view_name)
        textViewEmail = findViewById(R.id.tv_student_view_email)
        textViewDepartment = findViewById(R.id.tv_student_view_department)
        imageViewStudentImage = findViewById(R.id.iv_student_show_image)

        // Get intent extras.
        studentId = intent.getStringExtra(PublicConstants.STUDENT_ID)

        // Get the student details form database with student id.
        setStudentValues()
    }

    private fun setStudentValues() {
        val tableStudent = TableStudent()
        val student = tableStudent.viewStudent(studentId!!.toInt())

        // Set student data to text views.
        textViewName.text = StringBuilder("${student.firstName} ${student.lastName}")
        textViewEmail.text = student.lastName
        textViewDepartment.text = student.email

        // Set byte array to image view.
        imageViewStudentImage.setImageBitmap(
            BitmapFactory.decodeByteArray(
                student.image,
                0,
                student.image!!.size
            )
        )
    }
}