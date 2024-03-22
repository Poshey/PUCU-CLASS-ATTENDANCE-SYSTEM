package com.poshey.upuasatt2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class teacherScanner : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var scanButton: Button
    private lateinit var addButton: Button
    private lateinit var saveButton: Button
    private val scannedDataList = ArrayList<ScannedData>()
    private lateinit var adapter: ScannedDataAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacherscanner)

        recyclerView = findViewById(R.id.recyclerView)
        scanButton = findViewById(R.id.scanButton)
        addButton = findViewById(R.id.addButton)
        saveButton = findViewById(R.id.saveButton)
        adapter = ScannedDataAdapter(scannedDataList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnback = findViewById<ImageButton>(R.id.btnBack)
        btnback.setOnClickListener{

            finish()
        }

        scanButton.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }

        addButton.setOnClickListener {
            showManualInputDialog()
        }





        saveButton.setOnClickListener {
            saveDataToCSV()




        }

        adapter.setOnItemClickListener(object : ScannedDataAdapter.OnItemClickListener {
            override fun onEditClick(position: Int) {
                showEditDialog(position)
            }

            override fun onDeleteClick(position: Int) {
                removeItem(position)
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    val contents = result.contents
                    val delimiter = "\n"
                    val parts = contents.split(delimiter)

                    if (parts.size == 2) {
                        val scannedNames = parts[0].split(" ")  // Split the name into first name and last name
                        val scannedFname = scannedNames[0].trim()  // Assign first name
                        val scannedLname = scannedNames.getOrNull(1)?.trim() ?: ""  // Assign last name or empty string if not present
                        val scannedNumber = parts[1].trim()
                        val currentTime = getCurrentTime()

                        val scannedData = ScannedData(scannedFname, scannedLname, scannedNumber, currentTime)
                        scannedDataList.add(scannedData)
                        adapter.notifyItemInserted(scannedDataList.size - 1)
                    }

                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-dd-MM hh:mm a", Locale.getDefault())
        return dateFormat.format(Date(currentTimeMillis))
    }

    private fun showManualInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_manual_input, null)
        val firstNameInput = dialogView.findViewById<EditText>(R.id.firstNameInput)
        val lastNameInput = dialogView.findViewById<EditText>(R.id.lastNameInput)
        val numberInput = dialogView.findViewById<EditText>(R.id.numberInput)

        AlertDialog.Builder(this)
            .setTitle("Add Student")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val scannedFirstName = firstNameInput.text.toString().trim()
                val scannedLastName = lastNameInput.text.toString().trim()
                val scannedNumber = numberInput.text.toString().trim()
                val currentTime = getCurrentTime()

                if (scannedFirstName.isNotEmpty() && scannedLastName.isNotEmpty() && scannedNumber.isNotEmpty()) {
                    val scannedData = ScannedData(scannedFirstName, scannedLastName, scannedNumber, currentTime)
                    scannedDataList.add(scannedData)
                    adapter.notifyItemInserted(scannedDataList.size - 1)
                } else {
                    // Show a warning if any of the input fields are empty
                    Toast.makeText(this, "Please Complete Your Data", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    @SuppressLint("MissingInflatedId")
    private fun showEditDialog(position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_input, null)
        val firstNameInput = dialogView.findViewById<EditText>(R.id.editFirstNameInput)
        val lastNameInput = dialogView.findViewById<EditText>(R.id.editLastNameInput)
        val numberInput = dialogView.findViewById<EditText>(R.id.editNumberInput)

        val scannedData = scannedDataList[position]
        firstNameInput.setText(scannedData.fname)
        lastNameInput.setText(scannedData.lname)
        numberInput.setText(scannedData.number)

        AlertDialog.Builder(this)
            .setTitle("Edit Info")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val editedFirstName = firstNameInput.text.toString()
                val editedLastName = lastNameInput.text.toString()
                val editedNumber = numberInput.text.toString()

                if (editedFirstName.isNotBlank() && editedLastName.isNotBlank() && editedNumber.isNotBlank()) {
                    scannedDataList[position] = ScannedData(editedFirstName, editedLastName, editedNumber, scannedData.time)
                    adapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun removeItem(position: Int) {
        val scannedData = scannedDataList[position]

        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Remove Student?")
            .setPositiveButton("Yes") { _, _ ->
                scannedDataList.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }



    private fun saveDataToCSV() {
        val fileNameInput = EditText(this)
        fileNameInput.hint = "Enter a file name"

        AlertDialog.Builder(this)
            .setTitle("Save Data")
            .setMessage("Enter a name for the file")
            .setView(fileNameInput)
            .setPositiveButton("Save") { _, _ ->
                val fileName = fileNameInput.text.toString().trim()
                if (fileName.isNotEmpty()) {

                    val sharedPreferences = getSharedPreferences("TeacherAttendance", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val scannedDataListJson = gson.toJson(scannedDataList)
                    editor.putString("scannedDataList", scannedDataListJson)
                    editor.apply()




                    // Save data to a CSV file
                    saveDataToFile(fileName)
                } else {
                    Toast.makeText(this, "File name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }







    private fun saveDataToFile(fileName: String) {
        if (isExternalStorageWritable()) {
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

            if (documentsDir != null) {
                val file = File(documentsDir, "$fileName.csv")

                try {
                    val fileWriter = FileWriter(file)

                    // Write headers
                    fileWriter.append("First Name,Last Name,Student Number,Time In\n")

                    // Write data
                    for (scannedData in scannedDataList) {
                        fileWriter.append("${scannedData.fname},${scannedData.lname},${scannedData.number},${scannedData.time}\n")
                    }

                    fileWriter.flush()
                    fileWriter.close()

                    Toast.makeText(this, "Data saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Documents directory not available", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "External storage not writable", Toast.LENGTH_SHORT).show()
        }
    }




    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }


    companion object {
        private const val DIRECTORY_PICKER_REQUEST_CODE = 123
    }



}




