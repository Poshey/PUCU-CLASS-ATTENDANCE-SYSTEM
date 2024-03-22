package com.poshey.upuasatt2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class studentDashboard : AppCompatActivity() {

    private lateinit var qrCode: ImageView
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etNum: EditText
    private lateinit var generate: Button
    private lateinit var viewatt: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.studentdashboard)

        qrCode = findViewById(R.id.qrCode)
        etFirstName = findViewById(R.id.stFirstName)
        etLastName = findViewById(R.id.stLastName)
        etNum = findViewById(R.id.stNum)
        generate = findViewById(R.id.genQr)

        val btnback = findViewById<ImageButton>(R.id.btnBack)
        btnback.setOnClickListener{
            startActivity(Intent(this, LandingScreen::class.java))
            finish()
        }


        generate.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val number = etNum.text.toString()

            if(firstName.isEmpty() || lastName.isEmpty() || number.isEmpty()){
                Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show()
            }else{
                val data = "${firstName} ${lastName}\n$number"
                generateQRCode(data)
            }
        }
    }

    private fun generateQRCode(data: String) {
        val writer = QRCodeWriter()
        try{
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE,512,512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for(x in 0 until width){
                for(y in 0 until height){
                    bmp.setPixel(x,y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            qrCode.setImageBitmap(bmp)
        }catch (e: WriterException){
            e.printStackTrace()
        }
    }
}
