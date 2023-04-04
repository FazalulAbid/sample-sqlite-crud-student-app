package com.example.week5studentsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.week5studentsapp.HelperClasses.Database.DBConnection
import com.example.week5studentsapp.HelperClasses.General.PublicConstants

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        PublicConstants.appContext = this
        // Connect to database and create tables.
        val db = DBConnection(this)

        // Make screen finish after 3 secs using postDelayed
        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            finishAffinity()
        }, 2000)
    }
}