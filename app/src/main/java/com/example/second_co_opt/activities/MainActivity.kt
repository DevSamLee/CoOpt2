// MainActivity.kt Documentation

package com.example.second_co_opt.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.second_co_opt.R

// MainActivity class extending AppCompatActivity as the base class for activities
class MainActivity : AppCompatActivity() {

    // Late-initialized properties for buttons
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to the layout defined in activity_main.xml
        setContentView(R.layout.activity_main)

        // Initialize button references from the layout
        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        // Set click listener for the Insert Data button
        btnInsertData.setOnClickListener {
            // Create an intent to start the InsertionActivity
            val intent = Intent(this, InsertionActivity::class.java)
            // Start the InsertionActivity
            startActivity(intent)
        }

        // Set click listener for the Fetch Data button
        btnFetchData.setOnClickListener {
            // Create an intent to start the FetchingActivity
            val intent = Intent(this, FetchingActivity::class.java)
            // Start the FetchingActivity
            startActivity(intent)
        }
    }
}


