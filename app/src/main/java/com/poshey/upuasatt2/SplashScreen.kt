package com.poshey.upuasatt2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        val spScreen = findViewById<ImageView>(R.id.logomain)

        spScreen.alpha = 0f
        spScreen.animate().setDuration(3500).alpha(1f).withEndAction(){
            val intent = Intent(this, LandingScreen::class.java) // Dito lalagay yung main page
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

    }
}