package com.example.week5studentsapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week5studentsapp.HelperClasses.DataClasses.Student
import com.example.week5studentsapp.HelperClasses.Database.TableStudent
import com.example.week5studentsapp.HelperClasses.General.CommonlyUsedFunctions
import com.example.week5studentsapp.HelperClasses.General.PublicConstants
import com.google.android.material.textfield.TextInputEditText

class StudentCreateActivity : AppCompatActivity() {
    //Declare views.
    private lateinit var imageViewStudentImage: ImageView
    private lateinit var inputTextFirstName: TextInputEditText
    private lateinit var inputTextLastName: TextInputEditText
    private lateinit var inputTextEmail: TextInputEditText
    private lateinit var inputTextDepartment: TextInputEditText
    private lateinit var buttonCreateStudent: Button
    private lateinit var textViewFormMode: TextView

    // Declare objects for student image.
    private var imageUri: Uri? = null
    private lateinit var bitmapStudentImage: Bitmap

    // Set default form submit mode as add.
    private var formSubmitMode = PublicConstants.FORM_SUBMIT_MODE_ADD

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    // Variables.
    private var studentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_create)

        // Initialize variables and views.
        initializeComponents()
    }

    private fun initializeComponents() {
        // View Hooks.
        imageViewStudentImage = findViewById(R.id.iv_student_image)
        inputTextFirstName = findViewById(R.id.et_student_first_name)
        inputTextLastName = findViewById(R.id.et_student_last_name)
        inputTextEmail = findViewById(R.id.et_student_email)
        inputTextDepartment = findViewById(R.id.et_student_department)
        buttonCreateStudent = findViewById(R.id.btn_create_student)
        textViewFormMode = findViewById(R.id.tv_student_create_or_update)

        // Get intent extras.
        formSubmitMode = intent.getIntExtra(
            PublicConstants.FORM_SUBMIT_MODE, PublicConstants.FORM_SUBMIT_MODE_ADD
        )

        // Get student id if the form submit mode is edit.
        if (formSubmitMode == PublicConstants.FORM_SUBMIT_MODE_EDIT) {
            studentId = intent.getStringExtra(PublicConstants.STUDENT_ID)!!.toInt()

            // Set existing student details to edit texts.
            setStudentDetailsToInputFields(studentId)

            //Set form mode text view text to update.
            textViewFormMode.text = getString(R.string.update)
        } else {
            // Set student bitmap image as default image initially and reduce the size.
            bitmapStudentImage = CommonlyUsedFunctions.resizeBitmap(
                BitmapFactory.decodeResource(
                    this.resources, R.drawable.default_pic
                )
            )

            //Set form mode text view text to create.
            textViewFormMode.text = getString(R.string.create)
        }

        // Change button text to update if it is update mode.
        if (formSubmitMode == PublicConstants.FORM_SUBMIT_MODE_EDIT) buttonCreateStudent.text =
            resources.getText(R.string.update_student)

        // Set onClick listener to student image view.
        imageViewStudentImage.setOnClickListener {
            pickImageFromGallery()
        }

        // Set onClick listener to student add or update button
        buttonCreateStudent.setOnClickListener {
            if (formValidation())
                saveStudentData()
            else
                Toast.makeText(this, getString(R.string.form_validation_failed), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStudentDetailsToInputFields(studentId: Int) {
        //Get student data from database.
        val tableStudent = TableStudent()
        val student = tableStudent.viewStudent(studentId)

        // Set student data to input fields.
        inputTextFirstName.setText(student.firstName)
        inputTextLastName.setText(student.lastName)
        inputTextEmail.setText(student.email)
        inputTextDepartment.setText(student.department)

        // Set byte array to image view.
        bitmapStudentImage = BitmapFactory.decodeByteArray(student.image, 0, student.image!!.size)
        imageViewStudentImage.setImageBitmap(bitmapStudentImage)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageUri = data?.data

            // Get image bitmap and reduce the size of image.
            bitmapStudentImage = CommonlyUsedFunctions.resizeBitmap(
                MediaStore.Images.Media.getBitmap(
                    contentResolver, imageUri
                )
            )
            imageViewStudentImage.setImageBitmap(bitmapStudentImage)
        }
    }

    private fun saveStudentData() {
        // Set student details to Student.
        val student = Student(
            id = studentId,
            firstName = inputTextFirstName.text.toString(),
            lastName = inputTextLastName.text.toString(),
            email = inputTextEmail.text.toString(),
            department = inputTextDepartment.text.toString(),
            image = CommonlyUsedFunctions.bitmapToByteArray(bitmapStudentImage)
        )

        PublicConstants.appContext = this
        // Store student in database.
        val tableStudent = TableStudent()
        if (tableStudent.saveStudent(student, formSubmitMode)) {
            Toast.makeText(this, getString(R.string.student_saved), Toast.LENGTH_SHORT).show()

            // Finish this activity and go back to the student list activity.
            finish()
        }
    }

    private fun formValidation(): Boolean {
        if (inputTextFirstName.text.toString() == "" ||
            inputTextLastName.text.toString() == "" ||
            inputTextEmail.text.toString() == "" ||
            inputTextDepartment.text.toString() == ""
        ) {
            return false
        }
        return true
    }
}