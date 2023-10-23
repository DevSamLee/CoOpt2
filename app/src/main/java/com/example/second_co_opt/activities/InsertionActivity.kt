// InsertionActivity.kt Documentation

package com.example.second_co_opt.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.second_co_opt.R
import com.example.second_co_opt.models.EmployeeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// InsertionActivity class extending AppCompatActivity as the base class for activities
class InsertionActivity : AppCompatActivity() {

    // Late-initialized properties for UI elements
    private lateinit var etEmpName: EditText
    private lateinit var etEmpAge: EditText
    private lateinit var etEmpSalary: EditText
    private lateinit var btnSaveData: Button

    // Reference to the Firebase Realtime Database
    private lateinit var dbRef: DatabaseReference

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_insertion.xml
        setContentView(R.layout.activity_insertion)

        // Initialize UI elements from the layout
        etEmpName = findViewById(R.id.etEmpName)
        etEmpAge = findViewById(R.id.etEmpAge)
        etEmpSalary = findViewById(R.id.etEmpSalary)
        btnSaveData = findViewById(R.id.btnSave)

        // Get reference to the "Employees" node in the Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        // Set click listener for the Save Data button
        btnSaveData.setOnClickListener {
            // Call the function to save employee data
            saveEmployeeData()
        }
    }

    // Function to save employee data to the Firebase Realtime Database
    private fun saveEmployeeData() {

        // Retrieve employee data from UI elements
        val empName = etEmpName.text.toString()
        val empAge = etEmpAge.text.toString()
        val empSalary = etEmpSalary.text.toString()

        // Validate user input
        if (empName.isEmpty()) {
            etEmpName.error = "Please enter name"
            return
        }
        if (empAge.isEmpty()) {
            etEmpAge.error = "Please enter age"
            return
        }
        if (empSalary.isEmpty()) {
            etEmpSalary.error = "Please enter salary"
            return
        }

        // Generate a unique employee ID using push().key
        val empId = dbRef.push().key!!

        // Create an EmployeeModel object with the provided data
        val employee = EmployeeModel(empId, empName, empAge, empSalary)

        // Save the employee data to the "Employees" node in the Firebase Realtime Database
        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                // Display a success message to the user
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                // Clear input fields
                etEmpName.text.clear()
                etEmpAge.text.clear()
                etEmpSalary.text.clear()
            }
            .addOnFailureListener { err ->
                // Display an error message to the user in case of failure
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
