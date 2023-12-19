package com.aldiperdana.mobilestayawake.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.aldiperdana.mobilestayawake.R

interface BusinessFieldListener {
    fun onBusinessFieldSelected(businessField: String)
}

class BusinessFieldAdapter(
    private val businessFields: List<String>,
    private val businessFieldListener: BusinessFieldListener,
    private var selectedBusinessField: String?
) : RecyclerView.Adapter<BusinessFieldAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val businessFieldRadioButton: RadioButton = itemView.findViewById(R.id.businessFieldRadioButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_business_field, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val businessField = businessFields[position]
        holder.businessFieldRadioButton.text = businessField

        holder.businessFieldRadioButton.isChecked = (selectedBusinessField == businessField)
        holder.businessFieldRadioButton.setOnClickListener {
            selectedBusinessField = businessField
            notifyDataSetChanged()

            businessFieldListener.onBusinessFieldSelected(selectedBusinessField ?: "")
        }
    }

    override fun getItemCount(): Int = businessFields.size
}