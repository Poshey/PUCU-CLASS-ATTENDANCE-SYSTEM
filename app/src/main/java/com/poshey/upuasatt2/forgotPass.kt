package com.poshey.upuasatt2

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class forgotPass : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirm_pass: EditText
    private lateinit var create: Button
    private lateinit var btnback: ImageButton
    private lateinit var UserSelect: RadioGroup

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        create = findViewById(R.id.C_acc)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        confirm_pass = findViewById(R.id.con_password)
        UserSelect = findViewById(R.id.logInAs)

        btnback = findViewById(R.id.btnBack)

        btnback.setOnClickListener {
            finish()
        }

        create.setOnClickListener {
            val checkId = UserSelect.checkedRadioButtonId

            if (checkId == -1) {
                Toast.makeText(this, "Select Log in Type", Toast.LENGTH_SHORT).show()
            } else {
                val uname = username.text.toString()
                val pass = password.text.toString()
                val cpass = confirm_pass.text.toString()

                when (checkId) {
                    R.id.teacher -> {
                        if (uname.isEmpty()) {
                            Toast.makeText(this, "Please Enter your Username", Toast.LENGTH_SHORT).show()
                        } else if (pass.isEmpty()) {
                            Toast.makeText(this, "Please Enter New Password", Toast.LENGTH_SHORT).show()
                        } else if (cpass != pass) {
                            Toast.makeText(this, "Password Does Not Match", Toast.LENGTH_SHORT).show()
                        } else {
                            // Save new password
                            saveNewPassword(uname, pass)
                            teacherCode()
                        }
                    }
                    R.id.student -> {
                        if (uname.isEmpty()) {
                            Toast.makeText(this, "Please Enter your Username", Toast.LENGTH_SHORT).show()
                        } else if (pass.isEmpty()) {
                            Toast.makeText(this, "Please Enter New Password", Toast.LENGTH_SHORT).show()
                        } else if (cpass != pass) {
                            Toast.makeText(this, "Password Does Not Match", Toast.LENGTH_SHORT).show()
                        } else {
                            // Save new password
                            saveNewPassword(uname, pass)
                            val intent = Intent(this, studentDashboard::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun saveNewPassword(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    private fun teacherCode() {
        val inputEditText = EditText(this)

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enter Teacher Code")
        dialog.setView(inputEditText)

        dialog.setPositiveButton("OK") { _: DialogInterface, _: Int ->
            val facultyCode = inputEditText.text.toString()
            if (facultyCode == "teacher123") {
                val intent = Intent(this, teacherDashboard::class.java)
                intent.putExtra("username", username.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Wrong Teacher Code", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            // Handle the cancel button click (if needed)
        }

        dialog.show()
    }
}
