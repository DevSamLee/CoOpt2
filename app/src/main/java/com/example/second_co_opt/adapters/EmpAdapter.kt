// EmpAdapter.kt Documentation

package com.example.second_co_opt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.second_co_opt.R
import com.example.second_co_opt.models.EmployeeModel

// EmpAdapter class extending RecyclerView.Adapter
class EmpAdapter(private val empList: ArrayList<EmployeeModel>) :
    RecyclerView.Adapter<EmpAdapter.ViewHolder>() {

    // Interface for item click events
    private lateinit var mListener: onItemClickListener

    // Setter method to set the item click listener
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    // Function to create ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the emp_list_item layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.emp_list_item, parent, false)
        // Return a new ViewHolder instance with the inflated view and item click listener
        return ViewHolder(itemView, mListener)
    }

    // Function to bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Retrieve the current employee from the empList based on the position
        val currentEmp = empList[position]
        // Set the employee name to the TextView in the ViewHolder
        holder.tvEmpName.text = currentEmp.empName
    }

    // Function to get the total number of items in the RecyclerView
    override fun getItemCount(): Int {
        return empList.size
    }

    // ViewHolder class to hold references to UI elements for each item in the RecyclerView
    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        // TextView to display the employee name
        val tvEmpName: TextView = itemView.findViewById(R.id.tvEmpName)

        // Initialization block to set a click listener for the item view
        init {
            itemView.setOnClickListener {
                // Trigger the onItemClick method of the listener when an item is clicked
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    // Interface for item click events
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }
}
