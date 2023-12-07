package com.aldiperdana.mobilestayawake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldiperdana.mobilestayawake.adapter.BusinessFieldAdapter
import com.aldiperdana.mobilestayawake.adapter.BusinessFieldListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.aldiperdana.mobilestayawake.databinding.ActivityCompanyBinding

class CompanyActivity : AppCompatActivity() , BusinessFieldListener{

    private lateinit var binding: ActivityCompanyBinding
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var selectedBusinessField: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val businessFieldInput: TextView = findViewById(R.id.businessFieldInput)
        businessFieldInput.setOnClickListener {
            bottomSheetBusinessField()
        }
    }


            private fun bottomSheetBusinessField() {
                val bottomSheet: View =
                    layoutInflater.inflate(R.layout.item_bottom_sheet_business_field, null)
                bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
                bottomSheetDialog?.setContentView(bottomSheet)

                val recyclerView: RecyclerView = bottomSheet.findViewById(R.id.rvBusinessFields)
                val businessFields = createBusinessFields()
                val adapter = BusinessFieldAdapter(businessFields, this, selectedBusinessField)

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter

                bottomSheetDialog?.show()
            }

            override fun onBusinessFieldSelected(businessField: String) {
                val businessFieldInput: TextView = findViewById(R.id.businessFieldInput)
                businessFieldInput.text = businessField
                selectedBusinessField = businessField
                bottomSheetDialog?.dismiss()
            }

            private fun createBusinessFields(): List<String> {
                return listOf(
                    "Transportation Services",
                    "Public Transportation",
                    "Automotive Education and Training",
                    "Logistics and Freight Forwarding",
                    "Fleet Management",
                    "Vehicle Rental",
                    "Health and Ambulance Service",
                    "Vehicle manufacturer"
                )
            }
        }