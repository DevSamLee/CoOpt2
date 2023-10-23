// EmployeeDetailsActivity.kt Documentation

package com.example.second_co_opt.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.second_co_opt.R
import com.example.second_co_opt.models.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

// EmployeeDetailsActivity class extending AppCompatActivity as the base class for activities
class EmployeeDetailsActivity : AppCompatActivity() {

    // Late-initialized properties for UI elements
    private lateinit var tvEmpId: TextView
    private lateinit var tvEmpName: TextView
    private lateinit var tvEmpAge: TextView
    private lateinit var tvEmpSalary: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_employee_details.xml
        setContentView(R.layout.activity_employee_details)

        // Initialize UI elements
        initView()
        // Set values to the UI elements
        setValuesToViews()

        // Set click listener for the Update button
        btnUpdate.setOnClickListener {
            // Open the update dialog
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString()
            )
        }

        // Set click listener for the Delete button
        btnDelete.setOnClickListener {
            // Delete the record from the database
            deleteRecord(
                intent.getStringExtra("empId").toString()
            )
        }
    }

    // Function to open the update dialog
    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        // Create an AlertDialog
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        // Inflate the layout for the update dialog
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        // Initialize EditTexts and Button from the update dialog layout
        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        // Set initial values in EditTexts from the existing data
        etEmpName.setText(intent.getStringExtra("empName").toString())
        etEmpAge.setText(intent.getStringExtra("empAge").toString())
        etEmpSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        // Set click listener for the Update Data button in the dialog
        btnUpdateData.setOnClickListener {
            // Call the function to update employee data in the database
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmpSalary.text.toString()
            )

            // Show a toast message indicating that employee data is updated
            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

            // Update the TextViews with the new data
            tvEmpName.text = etEmpName.text.toString()
            tvEmpAge.text = etEmpAge.text.toString()
            tvEmpSalary.text = etEmpSalary.text.toString()

            // Dismiss the update dialog
            alertDialog.dismiss()
        }
    }

    // Function to update employee data in the database
    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = EmployeeModel(id, name, age, salary)
        dbRef.setValue(empInfo)
    }

    // Function to initialize UI elements
    private fun initView() {
        tvEmpId = findViewById(R.id.tvEmpId)
        tvEmpName = findViewById(R.id.tvEmpName)
        tvEmpAge = findViewById(R.id.tvEmpAge)
        tvEmpSalary = findViewById(R.id.tvEmpSalary)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    // Function to set values to TextViews from the intent
    private fun setValuesToViews() {
        tvEmpId.text = intent.getStringExtra("empId")
        tvEmpName.text = intent.getStringExtra("empName")
        tvEmpAge.text = intent.getStringExtra("empAge")
        tvEmpSalary.text = intent.getStringExtra("empSalary")
    }

    // Function to delete the record from the database
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            // Show a toast message indicating that employee data is deleted
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            // Create an intent to start the FetchingActivity and finish the current activity
            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            // Show a toast message in case of failure
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}
