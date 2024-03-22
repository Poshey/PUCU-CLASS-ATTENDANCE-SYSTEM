package com.poshey.upuasatt2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class LandingScreen : AppCompatActivity() {

    private lateinit var et_un: EditText
    private lateinit var et_pass: EditText
    private lateinit var btn_Sup: TextView
    private lateinit var btn_logIn: Button
    private lateinit var forgot: TextView
    private lateinit var UserSelect: RadioGroup
    private lateinit var btnback: ImageButton

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_screen)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        et_un = findViewById(R.id.username)
        et_pass = findViewById(R.id.password)
        btn_logIn = findViewById(R.id.logIn)
        UserSelect = findViewById(R.id.logInAs)
        btn_Sup = findViewById(R.id.signUp)
        forgot = findViewById(R.id.fpass)

        forgot.setOnClickListener {
            startActivity(Intent(this, forgotPass::class.java))
        }

        btn_Sup.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        btnback = findViewById(R.id.btnBack)
        btnback.setOnClickListener {
            finish()
        }

        btn_logIn.setOnClickListener {
            val checkId = UserSelect.checkedRadioButtonId

            if (checkId == -1) {
                Toast.makeText(this, "Select Log in Type", Toast.LENGTH_SHORT).show()
            } else {
                val username = et_un.text.toString()
                val password = et_pass.text.toString()

                val savedUsername = sharedPreferences.getString("username", "")
                val savedPassword = sharedPreferences.getString("password", "")

                when (checkId) {
                    R.id.teacher -> {
                        if (username.isEmpty()) {
                            Toast.makeText(this, "Please Enter your Username", Toast.LENGTH_SHORT).show()
                        } else if (password.isEmpty()) {
                            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show()
                        } else if (username != savedUsername || password != savedPassword) {
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, teacherDashboard::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            finish()
                        }
                    }
                    R.id.student -> {
                        if (username.isEmpty()) {
                            Toast.makeText(this, "Please Enter your Username", Toast.LENGTH_SHORT).show()
                        } else if (password.isEmpty()) {
                            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show()
                        } else if (username != savedUsername || password != savedPassword) {
                            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        } else {
                            val intent = Intent(this, studentDashboard::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun saveCredentials(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
}
