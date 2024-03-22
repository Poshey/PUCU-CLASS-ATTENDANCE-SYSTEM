package com.poshey.upuasatt2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class teacherDashboard : AppCompatActivity() {

    private lateinit var newAtt: Button
    private lateinit var viewAtt: Button
    private lateinit var tchrName: TextView
    private lateinit var StuButton: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)

        newAtt = findViewById(R.id.Attbutton)
        viewAtt = findViewById(R.id.Listbutton)
        StuButton = findViewById(R.id.StuButton)
        tchrName = findViewById(R.id.username)
        val username = intent.getStringExtra("username")
        val btnback = findViewById<ImageButton>(R.id.btnBack)
        btnback.setOnClickListener{
            startActivity(Intent(this, LandingScreen::class.java))
            finish()
        }

        StuButton.setOnClickListener{
            Toast.makeText(this, "This feature is under development", Toast.LENGTH_SHORT).show()
        }

        tchrName.text = "Prof. ${username}"


        newAtt.setOnClickListener{


            val intent = Intent(this, teacherScanner::class.java)
            startActivity(intent)

        }

        viewAtt.setOnClickListener{
            val intent = Intent(this, Teacher_Attendance_Viewing::class.java)
            startActivity(intent)

        }

    }


}