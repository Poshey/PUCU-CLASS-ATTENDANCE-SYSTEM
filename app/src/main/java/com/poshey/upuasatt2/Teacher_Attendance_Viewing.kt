package com.poshey.upuasatt2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class Teacher_Attendance_Viewing : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnBack: ImageButton
    private lateinit var adapter: ScannedDataAdapter
    private var scannedDataList: MutableList<ScannedData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_attendance_viewing)

        btnBack = findViewById(R.id.btnBack)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ScannedDataAdapter(scannedDataList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Clear the RecyclerView initially
        scannedDataList.clear()
        adapter.notifyDataSetChanged()

        val loadCsvButton: Button = findViewById(R.id.loadCsvButton)
        loadCsvButton.setOnClickListener {
            openFileChooser()
        }

        btnBack.setOnClickListener{

            finish()
        }
    }







    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, REQUEST_CODE_FILE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FILE_PICKER && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                inputStream?.let {
                    // Clear the existing data
                    scannedDataList.clear()
                    // Read and add data from the new file
                    scannedDataList.addAll(readCsvFile(it))
                    // Notify adapter about data change
                    adapter.notifyDataSetChanged()

                    // Remove the first item if the list is not empty
                    if (scannedDataList.isNotEmpty()) {
                        scannedDataList.removeAt(0)
                        adapter.notifyItemRemoved(0)
                    }
                } ?: run {
                    Toast.makeText(this, "Failed to open file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun readCsvFile(inputStream: InputStream): List<ScannedData> {
        val resultList = mutableListOf<ScannedData>()
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val parts = line?.split(",") ?: emptyList()
                if (parts.size == 4) {
                    val scannedData = ScannedData(parts[0], parts[1], parts[2], parts[3])
                    resultList.add(scannedData)
                }
            }
        }
        return resultList
    }

    companion object {
        private const val REQUEST_CODE_FILE_PICKER = 123
    }
}
