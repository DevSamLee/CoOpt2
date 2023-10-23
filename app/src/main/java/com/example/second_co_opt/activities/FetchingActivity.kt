// FetchingActivity.kt Documentation

package com.example.second_co_opt.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.second_co_opt.R
import com.example.second_co_opt.adapters.EmpAdapter
import com.example.second_co_opt.models.EmployeeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// FetchingActivity class extending AppCompatActivity as the base class for activities
class FetchingActivity : AppCompatActivity() {

    // Late-initialized properties for UI elements and data
    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_fetching.xml
        setContentView(R.layout.activity_fetching)

        // Initialize UI elements from the layout
        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        // Initialize employee list
        empList = arrayListOf<EmployeeModel>()

        // Call the function to retrieve employees' data
        getEmployeesData()
    }

    // Function to retrieve employees' data from Firebase Realtime Database
    private fun getEmployeesData() {

        // Hide RecyclerView and show loading text while data is being retrieved
        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        // Get reference to the "Employees" node in the Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        // Add a ValueEventListener to listen for changes in the data
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing employee list
                empList.clear()
                // Check if data exists in the snapshot
                if (snapshot.exists()) {
                    // Iterate through the snapshot to retrieve employee data
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        // Add employee data to the list
                        empList.add(empData!!)
                    }
                    // Create and set up the EmpAdapter with the updated employee list
                    val mAdapter = EmpAdapter(empList)
                    empRecyclerView.adapter = mAdapter

                    // Set item click listener for RecyclerView items
                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            // Create an intent to start the EmployeeDetailsActivity
                            val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            // Put extras (employee details) into the intent
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empName", empList[position].empName)
                            intent.putExtra("empAge", empList[position].empAge)
                            intent.putExtra("empSalary", empList[position].empSalary)

                            // Start the EmployeeDetailsActivity
                            startActivity(intent)
                        }
                    })

                    // Show RecyclerView and hide loading text after data is loaded
                    empRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event (not implemented in this example)
                // You may want to implement error handling here
            }
        })
    }
}

