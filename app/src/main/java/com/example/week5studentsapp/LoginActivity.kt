package com.example.week5studentsapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    companion object {
        //Shared preferences keys
        private const val SHARED_USERNAME_KEY: String = "USERNAME"
        private const val SHARED_PASSWORD_KEY: String = "PASSWORD"
        private const val SHARED_REMEMBER_ME_KEY: String = "REMEMBER_ME"
    }

    //    Declare Login Credentials
    private val username: String = "admin"
    private val password: String = "pass"

    //    Declare views
    private lateinit var checkBoxRememberMe: CheckBox
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var alertBoxBuilder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeComponents()
        getUserDataToInputFields()
    }

    private fun initializeComponents() {
//        Hooks for views
        editTextUsername = findViewById(R.id.et_login_username)
        editTextPassword = findViewById(R.id.et_login_password)
        checkBoxRememberMe = findViewById(R.id.cb_remember_me)

//        Create dialog box
        alertBoxBuilder = AlertDialog.Builder(this)
        alertBoxBuilder.setMessage("The username or password you entered is wrong, Please try again.")
        alertBoxBuilder.setTitle("Wrong credentials")
        alertBoxBuilder.setCancelable(true)
    }

    fun loginButtonOnClick(view: View) {
        if (editTextPassword.text.toString() == password && editTextUsername.text.toString() == username) {
//            Save the login data to shared preferences if needed
            saveLoginData()

            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)

//            Finish all previous activities
            finish()
            finishAffinity()
        } else {
            editTextUsername.error
            editTextPassword.error

//            Show alert that entered username and password is wrong
            alertBoxBuilder.show()

            Toast.makeText(this, getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveLoginData() {
        val username: String =
            if (checkBoxRememberMe.isChecked) editTextUsername.text.toString() else ""
        val password: String =
            if (checkBoxRememberMe.isChecked) editTextPassword.text.toString() else ""

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("rememberMe", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putString(SHARED_USERNAME_KEY, username)
            putString(SHARED_PASSWORD_KEY, password)
            putBoolean(SHARED_REMEMBER_ME_KEY, checkBoxRememberMe.isChecked)
        }.apply()
    }

    private fun getUserDataToInputFields() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("rememberMe", Context.MODE_PRIVATE)
        val rememberMe: Boolean = sharedPreferences.getBoolean(SHARED_REMEMBER_ME_KEY, false)

        if (rememberMe) {
            val username: String? = sharedPreferences.getString(SHARED_USERNAME_KEY, null)
            val password: String? = sharedPreferences.getString(SHARED_PASSWORD_KEY, null)
            //        Set username and password to fields
            editTextUsername.setText(username)
            editTextPassword.setText(password)
        }
        checkBoxRememberMe.isChecked = rememberMe
    }
}